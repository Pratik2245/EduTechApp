package com.example.javacp.Teacher;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.javacp.R;
import com.example.javacp.model.CoursesModelTeacher;


public class TeacherManageCourseActivity extends AppCompatActivity {
    private VideoView videoView;
    private ImageView ivThumbnail, btnPlay;
    private TextView tvTitle, tvDescription, tvPrice;
    private Button btnEnroll;
    private String videoUrl, thumbnailUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_manage_course);
        videoView = findViewById(R.id.videoView);
        ivThumbnail = findViewById(R.id.ivThumbnail);
        btnPlay = findViewById(R.id.btnPlay);
        tvTitle = findViewById(R.id.tvCourseTitle);
        tvDescription = findViewById(R.id.tvCourseDescription);
        tvPrice = findViewById(R.id.tvCoursePrice);
        btnEnroll = findViewById(R.id.EditCourse);

        // Get Data from Intent
        videoUrl = getIntent().getStringExtra("videoUrl");
        thumbnailUrl = getIntent().getStringExtra("thumbnailUrl");
        tvTitle.setText(getIntent().getStringExtra("title"));
        tvDescription.setText(getIntent().getStringExtra("description"));
        tvPrice.setText("₹" + getIntent().getStringExtra("price"));

        // Load Thumbnail with Glide
        String secureUrl = thumbnailUrl.replace("http://", "https://");
        Glide.with(this)
                .load(secureUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .into(ivThumbnail);

        // Set Play Button Click Listener
        btnPlay.setOnClickListener(v -> playVideo());
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


}