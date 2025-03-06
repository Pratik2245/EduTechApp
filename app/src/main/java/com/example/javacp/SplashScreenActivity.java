package com.example.javacp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        new Handler().postDelayed(this::checkUser, 500);
    }

    private void checkUser() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            getUserRole(user.getUid());
        } else {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        }
    }

    private void getUserRole(String userId) {
        List<String> collections = Arrays.asList("admin", "students", "teachers");

        List<Task<DocumentSnapshot>> tasks = Arrays.asList(
                db.collection("admin").document(userId).get(),
                db.collection("students").document(userId).get(),
                db.collection("teachers").document(userId).get()
        );

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(results -> {
            for (int i = 0; i < results.size(); i++) {
                DocumentSnapshot document = (DocumentSnapshot) results.get(i);
                if (document.exists()) {
                    String role = collections.get(i);
                    Toast.makeText(this, "role"+role, Toast.LENGTH_SHORT).show();// Role is the collection name
                    redirectToDashboard(role);
                    return;
                }
            }
            Toast.makeText(this, "User role not found.", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error retrieving role.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void redirectToDashboard(String role) {
        Class<?> targetActivity;
        switch (role) {
            case "admin":
                targetActivity = AdminHomeActivity.class;
                break;
            case "students":
                targetActivity = HomeActivityStudents.class;
                break;
            case "teachers":
                targetActivity = HomeActivityStudents.class; // Replace with actual teacher activity
                break;
            default:
                Toast.makeText(this, "Invalid user role.", Toast.LENGTH_SHORT).show();
                finish();
                return;
        }
        startActivity(new Intent(this, targetActivity));
        finish();
    }
}
