package com.example.javacp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog; // ✅ Import ProgressDialog
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
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
    private ProgressDialog progressDialog;  // ✅ Added ProgressDialog

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        loginRedirect = findViewById(R.id.loginRedirect);
        confirmPassword = findViewById(R.id.confirmPassword);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        fullName = findViewById(R.id.fullName);
        signupButton = findViewById(R.id.signupButton);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize the ProgressDialog
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Signing up..."); // Set loading message
        progressDialog.setCancelable(false); // Disable dismissing the dialog by tapping outside

        loginRedirect.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                String userFullName = fullName.getText().toString().trim();
                String userConfirmPass = confirmPassword.getText().toString().trim();

                if (userFullName.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Full Name is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userEmail.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(SignUpActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userPassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                    return;
                } else if (userPassword.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userConfirmPass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Confirm Password is required", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!userPassword.equals(userConfirmPass)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show ProgressDialog when sign-up starts
                progressDialog.show();

                // Disable the button to prevent multiple clicks
                signupButton.setEnabled(false);

                auth.fetchSignInMethodsForEmail(userEmail)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    List<String> signInMethods = task.getResult().getSignInMethods();
                                    if (signInMethods != null && !signInMethods.isEmpty()) {
                                        // Email is already registered
                                        Toast.makeText(SignUpActivity.this, "Email already in use. Please log in.", Toast.LENGTH_SHORT).show();
                                        hideProgress(); // Hide progress if email exists
                                    } else {
                                        // Email is not in use, proceed with signup
                                        createUser(userEmail, userPassword, userFullName);
                                    }
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Error checking email.", Toast.LENGTH_SHORT).show();
                                    hideProgress(); // Hide progress if error occurs
                                }
                            }
                        });
            }
        });
    }

    private void createUser(String userEmail, String userPassword, String userFullName) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser authUser = auth.getCurrentUser();
                            if (authUser != null) {
                                String userId = authUser.getUid();
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("fullName", userFullName);
                                userMap.put("email", userEmail);
                                userMap.put("role", "student");

                                db.collection("users").document(userId).set(userMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                                hideProgress(); // Hide ProgressDialog after success
                                                Intent intent = new Intent(SignUpActivity.this, HomeActiviyCourses.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(SignUpActivity.this, "Error saving data.", Toast.LENGTH_SHORT).show();
                                            hideProgress(); // Hide progress if Firestore fails
                                        });
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            hideProgress(); // Hide progress if signup fails
                        }
                    }
                });
    }

    private void hideProgress() {
        progressDialog.dismiss();  // Dismiss the ProgressDialog after the process is done
        signupButton.setEnabled(true);  // Enable the button again
    }
}
