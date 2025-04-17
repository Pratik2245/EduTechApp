package com.example.javacp.Teacher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.example.javacp.databinding.FragmentTeacherFrag1HomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TeacherFrag_1Home extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;
    private FirebaseFirestore firestore;
    private Uri thumbnailUri, videoUri;
    FragmentTeacherFrag1HomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeacherFrag1HomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firestore = FirebaseFirestore.getInstance();

        // Initialize Cloudinary
        initCloudinary();

        // Set onClick listeners for picking media
        binding.btnPickThumbnail.setOnClickListener(v -> pickImage());
        binding.btnPickVideo.setOnClickListener(v -> pickVideo());

        // Upload course button
        binding.btnUploadCourse.setOnClickListener(v -> uploadCourseDetails());

        return view;
    }

    private void initCloudinary() {
        try {
            MediaManager.get();  // Check if already initialized
        } catch (IllegalStateException e) {
            // If not initialized, initialize Cloudinary
            Map config = new HashMap();
            config.put("cloud_name", "dcucu5eie");  // Replace with your Cloud Name
            config.put("api_key", "365724184229992");  // Replace with your API Key
            config.put("api_secret", "C5OmPK7PsmNG0hOwaZBbkfkJgsI");  // Replace with your API Secret
            MediaManager.init(requireContext(), config);
        }
    }



    private void uploadCourseDetails() {
        String title = binding.etCourseTitle.getText().toString().trim();
        String description = binding.etCourseDescription.getText().toString().trim();
        String price = binding.etCoursePrice.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || price.isEmpty() || thumbnailUri == null || videoUri == null) {
            Toast.makeText(getContext(), "Please fill all fields and select media files", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload thumbnail to Cloudinary
        MediaManager.get().upload(thumbnailUri)
                .option("resource_type", "image")
                .callback(new com.cloudinary.android.callback.UploadCallback() {
                    @Override
                    public void onStart(String requestId) {}

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String thumbnailUrl = resultData.get("url").toString();
                        uploadVideo(title, description, price, thumbnailUrl);
                    }

                    @Override
                    public void onError(String requestId, com.cloudinary.android.callback.ErrorInfo error) {
                        Toast.makeText(getContext(), "Thumbnail upload failed: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, com.cloudinary.android.callback.ErrorInfo error) {}
                }).dispatch();
    }

    private void uploadVideo(String title, String description, String price, String thumbnailUrl) {
        MediaManager.get().upload(videoUri)
                .option("resource_type", "video")
                .callback(new com.cloudinary.android.callback.UploadCallback() {
                    @Override
                    public void onStart(String requestId) {}

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String videoUrl = resultData.get("url").toString();
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        String teacherUid = auth.getCurrentUser().getUid();

                        FirebaseFirestore.getInstance().collection("users")  // or "teachers"
                                .document(teacherUid)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        String teacherName = documentSnapshot.getString("fullName");  // Use your actual field key
                                        saveToFirestore(title, description, price, thumbnailUrl, videoUrl, teacherName);
                                    } else {
                                        Toast.makeText(getContext(), "Teacher info not found", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Failed to fetch teacher name: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    }

                    @Override
                    public void onError(String requestId, com.cloudinary.android.callback.ErrorInfo error) {
                        Toast.makeText(getContext(), "Video upload failed: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, com.cloudinary.android.callback.ErrorInfo error) {}
                }).dispatch();
    }

    private void pickVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                thumbnailUri = data.getData();
                binding.ivCourseThumbnail.setImageURI(thumbnailUri);  // Show image in ImageView
            } else if (requestCode == PICK_VIDEO_REQUEST) {
                videoUri = data.getData();
                binding.vvCourseVideo.setVideoURI(videoUri);  // Show video in VideoView

                // Add a MediaController
                MediaController mediaController = new MediaController(getContext());
                mediaController.setAnchorView(binding.vvCourseVideo);
                binding.vvCourseVideo.setMediaController(mediaController);

                binding.vvCourseVideo.requestFocus();
                binding.vvCourseVideo.start(); // Start playing the video
            }
        }
    }


    private void saveToFirestore(String title, String description, String price, String thumbnailUrl, String videoUrl,String teacherName) {
        String teacherUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> course = new HashMap<>();
        course.put("title", title);
        course.put("description", description);
        course.put("price", price);
        course.put("thumbnailUrl", thumbnailUrl);
        course.put("videoUrl", videoUrl);
        course.put("teacherUid", teacherUid);
        course.put("teacherName",teacherName);
        course.put("timestamp", System.currentTimeMillis());

        firestore.collection("courses")
                .add(course)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Course uploaded successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to upload course: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
