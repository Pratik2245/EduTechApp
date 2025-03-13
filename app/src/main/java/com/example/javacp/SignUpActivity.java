package com.example.javacp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    TextView loginRedirect;
    Button signupButton;
    EditText confirmPassword, password, email, fullName;
    FirebaseAuth auth;
    FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // If user is already logged in, redirect to their home screen
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            redirectToDashboard(currentUser.getUid());
        }

        loginRedirect = findViewById(R.id.loginRedirect);
        confirmPassword = findViewById(R.id.confirmPassword);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        fullName = findViewById(R.id.fullName);
        signupButton = findViewById(R.id.signupButton);

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Signing up...");
        progressDialog.setCancelable(false);

        loginRedirect.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
                return true;
            }
        });

        signupButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userFullName = fullName.getText().toString().trim();
        String userConfirmPass = confirmPassword.getText().toString().trim();

        if (userFullName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userConfirmPass.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPassword.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!userPassword.equals(userConfirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        signupButton.setEnabled(false);

        auth.fetchSignInMethodsForEmail(userEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> signInMethods = task.getResult().getSignInMethods();
                if (signInMethods != null && !signInMethods.isEmpty()) {
                    Toast.makeText(this, "Email already registered. Logging you in...", Toast.LENGTH_SHORT).show();
                    loginExistingUser(userEmail, userPassword);
                } else {
                    createNewUser(userEmail, userPassword, userFullName);
                }
            } else {
                Toast.makeText(this, "Error checking email.", Toast.LENGTH_SHORT).show();
                hideProgress();
            }
        });
    }

    private void loginExistingUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    redirectToDashboard(user.getUid());
                }
            } else {
                Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                hideProgress();
            }
        });
    }

    private void createNewUser(String userEmail, String userPassword, String userFullName) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser authUser = auth.getCurrentUser();
                if (authUser != null) {
                    String userId = authUser.getUid();
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("fullName", userFullName);
                    userMap.put("email", userEmail);
                    userMap.put("role", "student");

                    db.collection("users").document(userId).set(userMap).addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Sign-up Successful", Toast.LENGTH_SHORT).show();
                        redirectToDashboard(userId);
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Error saving user data.", Toast.LENGTH_SHORT).show();
                        hideProgress();
                    });
                }
            } else {
                Toast.makeText(this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                hideProgress();
            }
        });
    }

    private void redirectToDashboard(String userId) {
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String role = documentSnapshot.getString("role");
                Intent intent;
                if ("admin".equals(role)) {
                    intent = new Intent(SignUpActivity.this, AdminHomeActivity.class);
                } else if ("student".equals(role)){
                    intent = new Intent(SignUpActivity.this, HomeActivityStudents.class);
                }else {
                    intent=new Intent(SignUpActivity.this,TeacherHomeActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error retrieving user data.", Toast.LENGTH_SHORT).show();
            hideProgress();
        });
    }

    private void hideProgress() {
        progressDialog.dismiss();
        signupButton.setEnabled(true);
    }
}
