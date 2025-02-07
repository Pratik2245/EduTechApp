package com.example.javacp;

import android.annotation.SuppressLint;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
TextView loginRedirect;
Button signupButton;
EditText confirmPassword,password,email,fullName;
FirebaseAuth auth;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        loginRedirect=findViewById(R.id.loginRedirect);
        confirmPassword=findViewById(R.id.confirmPassword);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        fullName=findViewById(R.id.fullName);
        signupButton=findViewById(R.id.signupButton);
        auth=FirebaseAuth.getInstance();
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

        //onclick for button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userFullName = fullName.getText().toString();
                String userConfirmPass = confirmPassword.getText().toString();

                if (userFullName.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Full Name is required", Toast.LENGTH_SHORT).show();
                    return; // Return if validation fails
                }

                // Validate email format
                if (userEmail.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(SignUpActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate password
                if (userPassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                    return;
                } else if (userPassword.length() < 6) { // You can adjust the password length as per your requirements
                    Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate confirm password
                if (userConfirmPass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Confirm Password is required", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!userPassword.equals(userConfirmPass)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

//                Toast.makeText(SignUpActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(SignUpActivity.this,HomeActiviyCourses.class);
                            startActivity(intent);
                            finish();
                        } else {

                            Toast.makeText(SignUpActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }
}
