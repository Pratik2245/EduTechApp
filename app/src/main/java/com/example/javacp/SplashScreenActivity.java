package com.example.javacp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            redirectToDashboard();
        }else {
            new Handler().postDelayed(() -> {
                // Start the main activity
                Intent intent = new Intent(SplashScreenActivity.this, SignUpActivity.class);
                startActivity(intent);

                // Close the splash activity
                finish();
            }, 1000);
        }
    }
    private void redirectToDashboard() {
        // Get current user ID
        String userId = auth.getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        if ("admin".equals(role)) {
                            // Redirect to Admin Dashboard
                            Intent intent = new Intent(SplashScreenActivity.this, AdminHomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else if ("student".equals(role)) {
                            // Redirect to Student Dashboard
                            Intent intent = new Intent(SplashScreenActivity.this, HomeActiviyCourses.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SplashScreenActivity.this, "Error retrieving user data.", Toast.LENGTH_SHORT).show();
                });
    }
}