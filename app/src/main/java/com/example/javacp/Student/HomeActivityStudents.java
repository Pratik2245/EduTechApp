package com.example.javacp.Student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.javacp.Common.LoginActivity;
import com.example.javacp.R;
import com.example.javacp.databinding.ActivityHomeActiviyStudentsBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.PaymentResultListener;

import java.util.HashMap;
import java.util.Map;

public class HomeActivityStudents extends AppCompatActivity implements PaymentResultListener {
    ActivityHomeActiviyStudentsBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String currentUserId;

    // Variables to store last payment details
    private static String lastThumbnailUrl = "";
    private static String lastVideoUrl = "";
    private static String lastTeacherId = "";
    private static String lastTeacherName = "";

    private static String lastCourseTitle = "";
    private static String lastPaymentAmount = "";
    private static String lastCourseId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeActiviyStudentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            currentUserId = auth.getCurrentUser().getUid();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawer, binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();

        replaceFragments(new StudentFrag_1home());

        binding.bottomNavigation.setOnItemSelectedListener(menuItem -> {
            int selectedId = menuItem.getItemId();
            if (selectedId == R.id.nav_home) {
                replaceFragments(new StudentFrag_1home());
            } else if (selectedId == R.id.nav_course_details) {
                replaceFragments(new StudentFrag_2CourseDetails());
            } else if (selectedId == R.id.nav_video_player) {
                replaceFragments(new StudentFrag_3SubscribedVideos());
            } else if (selectedId == R.id.nav_progress) {
                replaceFragments(new StudentFrag_4CourseProgress());
            } else if (selectedId == R.id.nav_profile) {
                replaceFragments(new StudentFrag_5ProfileFragment());
            }
            return true;
        });

        // Navigation View
        binding.navigationViewStudent.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.home) {
                replaceFragments(new StudentFrag_1home());
            } else if (id == R.id.subscribedVideos) {
                replaceFragments(new StudentFrag_3SubscribedVideos());
            } else if (id == R.id.profileStudent) {
                replaceFragments(new StudentFrag_5ProfileFragment());
            } else if (id == R.id.logout) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomeActivityStudents.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivityStudents.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else if (id == R.id.coursesProgress) {
                replaceFragments(new StudentFrag_4CourseProgress());
            } else if (id == R.id.courseDetails) {
                replaceFragments(new StudentFrag_2CourseDetails());
            } else if (id == R.id.rating) {
                String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            } else if (id == R.id.share) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "Check out this amazing app: https://play.google.com/store/apps/details?id=" + getPackageName();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "App Recommendation");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
            binding.drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void replaceFragments(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment Successful! ID: " + razorpayPaymentID, Toast.LENGTH_LONG).show();
        savePaymentDetails(razorpayPaymentID);
        saveSubscribedCourse();
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment Failed: " + response, Toast.LENGTH_LONG).show();
    }

    private void savePaymentDetails(String paymentID) {
        if (currentUserId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("userId", currentUserId);
        paymentData.put("paymentID", paymentID);
        paymentData.put("amount", lastPaymentAmount);
        paymentData.put("courseTitle", lastCourseTitle);
        paymentData.put("timestamp", System.currentTimeMillis());

        db.collection("payments").document(paymentID)
                .set(paymentData)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Payment details saved successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save payment", Toast.LENGTH_SHORT).show());
    }

    private void saveSubscribedCourse() {
        if (currentUserId == null || lastCourseId == null || lastCourseId.isEmpty()) {
            Toast.makeText(this, "Cannot subscribe to course. Missing data: " + lastCourseId, Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String, Object> subscriptionData = new HashMap<>();
        subscriptionData.put("userId", currentUserId);
        subscriptionData.put("courseId", lastCourseId);
        subscriptionData.put("courseTitle", lastCourseTitle);
        subscriptionData.put("thumbnailUrl", lastThumbnailUrl);
        subscriptionData.put("videoUrl", lastVideoUrl);
        subscriptionData.put("teacherId", lastTeacherId);
        subscriptionData.put("teacherName", lastTeacherName);
        subscriptionData.put("subscribedAt", System.currentTimeMillis());

        db.collection("subscribed_courses")
                .add(subscriptionData)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Subscribed to course successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to subscribe course: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }



    public static void setLastPaymentDetails(String courseTitle, String amount, String courseId,
                                             String thumbnailUrl, String videoUrl, String teacherId, String teacherName) {
        lastCourseTitle = courseTitle;
        lastPaymentAmount = amount;
        lastCourseId = courseId;
        lastThumbnailUrl = thumbnailUrl;
        lastVideoUrl = videoUrl;
        lastTeacherId = teacherId;
        lastTeacherName = teacherName;
    }


}