package com.example.javacp.Student;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import com.example.javacp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StudentStartCourseActivity extends AppCompatActivity {
    private StyledPlayerView playerView;
    private ExoPlayer player;
    private ImageButton btnFullscreen;
    private boolean isFullscreen = false;

    // Add these:
    private String videoUrl, courseTitle, thumbnail, teacherName, teacherId, studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_start_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        playerView = findViewById(R.id.playerView);
        btnFullscreen = findViewById(R.id.btnFullscreen);

        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        btnFullscreen.setOnClickListener(v -> toggleFullscreen());

        // Get data from intent
        videoUrl = getIntent().getStringExtra("videoUrl");
        courseTitle = getIntent().getStringExtra("courseTitle");
        thumbnail = getIntent().getStringExtra("thumbnail");
        teacherName = getIntent().getStringExtra("teacherName");
        teacherId = getIntent().getStringExtra("teacherId");
        studentId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        if (videoUrl != null) {
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();

            // Restore the previous playback position
            long savedPosition = getSharedPreferences("player_state", MODE_PRIVATE)
                    .getLong("current_position", 0);
            if (savedPosition > 0) {
                player.seekTo(savedPosition);
            }
        }

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    player.seekTo(0);
                    player.pause();
                }
            }
        });
    }

    private void toggleFullscreen() {
        if (isFullscreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isFullscreen = false;
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isFullscreen = true;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isFullscreen = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isFullscreen = false;
        }

        // Restore position after configuration change
        long savedPosition = getSharedPreferences("player_state", MODE_PRIVATE)
                .getLong("current_position", 0);
        if (savedPosition > 0) {
            player.seekTo(savedPosition);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveProgressToFirestore();
        // Save the current position of the video player
        if (player != null) {
            long currentPosition = player.getCurrentPosition();
            getSharedPreferences("player_state", MODE_PRIVATE)
                    .edit()
                    .putLong("current_position", currentPosition)
                    .apply();
        }
        player.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveProgressToFirestore();
        player.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.play();
    }

    private void saveProgressToFirestore() {
        if (player == null || player.getDuration() <= 0) return;

        long position = player.getCurrentPosition();
        long duration = player.getDuration();
        float progressPercent = (position * 100f) / duration;

        Map<String, Object> progressData = new HashMap<>();
        progressData.put("studentId", studentId);
        progressData.put("teacherId", teacherId);
        progressData.put("teacherName", teacherName);
        progressData.put("courseTitle", courseTitle);
        progressData.put("thumbnail", thumbnail);
        progressData.put("videoUrl", videoUrl);
        progressData.put("watchedDuration", position);
        progressData.put("totalDuration", duration);
        progressData.put("progressPercent", progressPercent);
        progressData.put("lastUpdated", FieldValue.serverTimestamp());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("course_progress")
                .document(studentId + "_" + courseTitle)
                .set(progressData)
                .addOnSuccessListener(unused -> {
                    Log.d("progress", "Progress Stored Successfully");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error in storing the progress", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Go back when the back button is pressed
        return true;
    }
}
