package com.example.javacp.Teacher;


import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.javacp.R;
import com.example.javacp.model.CoursesModelTeacher;
import com.google.firebase.firestore.FirebaseFirestore;


public class TeacherManageCourseActivity extends AppCompatActivity {
    private VideoView videoView;
    private EditText etTitle, etDescription, etPrice;
    private FirebaseFirestore firestore;

    private ImageView ivThumbnail, btnPlay;
    private Button updateCourseData;
    private String videoUrl, thumbnailUrl,teacherUID,courseID;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_manage_course);
        videoView = findViewById(R.id.videoView);
        ivThumbnail = findViewById(R.id.ivThumbnail);
        btnPlay = findViewById(R.id.btnPlay);
        updateCourseData = findViewById(R.id.updateCourseData);
        etTitle = findViewById(R.id.etCourseTitle);
        etDescription = findViewById(R.id.etCourseDescription);
        etPrice = findViewById(R.id.etCoursePrice);

        // Get Data from Intent
        teacherUID = getIntent().getStringExtra("teacherUID");
        courseID = getIntent().getStringExtra("courseID");
        Toast.makeText(this, "teacherid="+courseID, Toast.LENGTH_SHORT).show();
        videoUrl = getIntent().getStringExtra("videoUrl");
        thumbnailUrl = getIntent().getStringExtra("thumbnailUrl");
        etTitle.setText(getIntent().getStringExtra("title"));
        etDescription.setText(getIntent().getStringExtra("description"));
        etPrice.setText(getIntent().getStringExtra("price"));
        // Firestore instance
        firestore = FirebaseFirestore.getInstance();

        // Load Thumbnail with Glide
        String secureUrl = thumbnailUrl.replace("http://", "https://");
        Glide.with(this)
                .load(secureUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .into(ivThumbnail);

        // Set Play Button Click Listener
        btnPlay.setOnClickListener(v -> playVideo());
        updateCourseData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCourseDetails();
            }
        });
    }

    private void playVideo() {
        if (videoUrl != null && !videoUrl.isEmpty()) {
            videoUrl = videoUrl.replace("http://", "https://");
            Uri uri = Uri.parse(videoUrl);
            videoView.setVideoURI(uri);

            videoView.setOnPreparedListener(mp -> {
                ivThumbnail.setVisibility(View.GONE);
                btnPlay.setVisibility(View.GONE);
                videoView.start();
            });

            videoView.setOnErrorListener((mp, what, extra) -> {
                Log.e("VideoView", "Error: " + what + ", Extra: " + extra);
                return true; // Handle the error
            });

            videoView.setOnCompletionListener(mp -> {
                ivThumbnail.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.VISIBLE);
            });

            videoView.start();
        } else {
            Log.e("VideoView", "Invalid video URL");
        }
    }

    private void updateCourseDetails() {
        String updatedTitle = etTitle.getText().toString().trim();
        String updatedDescription = etDescription.getText().toString().trim();
        String updatedPrice = etPrice.getText().toString().trim();

        if (updatedTitle.isEmpty() || updatedDescription.isEmpty() || updatedPrice.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("courses").document(courseID)
                .update(
                        "title", updatedTitle,
                        "description", updatedDescription,
                        "price", updatedPrice
                )
                .addOnSuccessListener(unused -> Toast.makeText(this, "Course updated successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }



}