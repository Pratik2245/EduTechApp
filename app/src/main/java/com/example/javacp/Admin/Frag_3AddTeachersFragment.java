package com.example.javacp.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.javacp.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Frag_3AddTeachersFragment extends Fragment {

    private EditText etFullName, etEmail, etPhone, etQualifications, etSpecialization, etExperience, etPassword;
    private MaterialButton btnSubmit;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_3_add_teachers, container, false);

        // Initialize UI elements
        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        etPhone = view.findViewById(R.id.etPhone);
        etQualifications = view.findViewById(R.id.etQualifications);
        etSpecialization = view.findViewById(R.id.etSpecialization);
        etExperience = view.findViewById(R.id.etExperience);
        etPassword = view.findViewById(R.id.etPassword);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Adding Teacher...");
        progressDialog.setCancelable(false);

        btnSubmit.setOnClickListener(v -> addTeacher());

        return view;
    }

    private void addTeacher() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String qualifications = etQualifications.getText().toString().trim();
        String specialization = etSpecialization.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();
        //String password = etPassword.getText().toString().trim();
        String password = generateRandomPassword(10);

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(qualifications) || TextUtils.isEmpty(specialization) ||
                TextUtils.isEmpty(experience) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    Map<String, Object> teacherMap = new HashMap<>();
                    teacherMap.put("fullName", fullName);
                    teacherMap.put("email", email);
                    teacherMap.put("phone", phone);
                    teacherMap.put("qualifications", qualifications);
                    teacherMap.put("specialization", specialization);
                    teacherMap.put("experience", experience);
                    teacherMap.put("role", "teacher");

                    db.collection("users").document(userId).set(teacherMap)
                            .addOnSuccessListener(unused -> {
                                progressDialog.dismiss();

                                String subject = "Welcome to the Teacher Portal";
                                String message = "Hi " + fullName + ",\n\n" +
                                        "Your account has been created successfully.\n\n" +
                                        "Login credentials:\n" +
                                        "Email: " + email + "\n" +
                                        "Password: " + password + "\n\n" +
                                        "Regards,\nAdmin Team";

                                // Calling the JavaMailAPI
                                if(email.isEmpty()){
                                    return;
                                }
                                sendEmail(subject,message,email);

                                Toast.makeText(getContext(), "Teacher added successfully", Toast.LENGTH_SHORT).show();
                                clearFields();
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                progressDialog.dismiss();
                Log.d("Error In add teacher",task.getException().toString());
                Toast.makeText(getContext(), "Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendEmail(String subject, String message, String email) {
        Intent i=new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL,new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT,subject);
        i.putExtra(Intent.EXTRA_TEXT,message);
        i.setType("message/rfc822");
        startActivity(Intent.createChooser(i,"choose an email client: "));
    }
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }
    private void clearFields() {
        etFullName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etQualifications.setText("");
        etSpecialization.setText("");
        etExperience.setText("");
        etPassword.setText("");
    }
}