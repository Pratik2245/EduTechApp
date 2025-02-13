package com.example.javacp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    TextView signupRedirect;
    private ProgressDialog progressDialog; // Declare ProgressDialog
    FirebaseAuth auth;
    private EditText emailEditText, passwordEditText; // Added confirmPasswordEditText
    Button loginButton;
    Spinner userRoleSpinner; // Declare Spinner for role selection

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        signupRedirect = findViewById(R.id.signupRedirect);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password); // Initialize confirm password field
        loginButton = findViewById(R.id.loginButton);
        userRoleSpinner = findViewById(R.id.userRoleSpinner); // Initialize Spinner

        // Initialize the ProgressDialog
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging in..."); // Set loading message
        progressDialog.setCancelable(false); // Prevent dismiss by tapping outside

        signupRedirect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    // Redirect to SignUpActivity
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String selectedRole = userRoleSpinner.getSelectedItem().toString(); // Get selected role

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (selectedRole.equals("Select a role")) {
                Toast.makeText(LoginActivity.this, "Please select a role", Toast.LENGTH_SHORT).show();
            } else if (!isValidEmail(email)) {
                Toast.makeText(LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            } else if (!isValidPassword(password)) {
                Toast.makeText(LoginActivity.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            }  else {
                loginUser(email, password, selectedRole);
            }
        });
    }

    private void loginUser(String email, String password, String selectedRole) {
        // Show the ProgressDialog before starting login process
        progressDialog.show();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Dismiss the ProgressDialog after the login attempt is finished
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Log.d("Login", "Success: " + user.getEmail());

                        // Check the role and redirect accordingly
                        if (selectedRole.equals("admin")) {
                            // Redirect to Admin Dashboard
                            Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class); // Replace with your Admin activity
                            startActivity(intent);
                            finish();
                        } else if (selectedRole.equals("student")) {
                            // Redirect to Student Dashboard
                            Intent intent = new Intent(LoginActivity.this, HomeActiviyCourses.class); // Replace with your Student activity
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.e("Login", "Failed: " + task.getException().getMessage());
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Email Validation
    private boolean isValidEmail(String email) {
        // Use regex to validate email format
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        return email.matches(emailPattern);
    }

    // Password Validation (minimum 6 characters)
    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
}
