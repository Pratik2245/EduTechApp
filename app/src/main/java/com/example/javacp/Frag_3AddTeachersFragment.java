package com.example.javacp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Frag_3AddTeachersFragment extends Fragment {

    private EditText etFullName, etEmail, etPhone, etQualifications, etSpecialization, etExperience, etPassword;
    private MaterialButton btnSubmit, btnUploadImage;
    private ImageView imgProfilePicture;
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
        btnUploadImage = view.findViewById(R.id.btnUploadImage);
        imgProfilePicture = view.findViewById(R.id.imgProfilePicture);

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
        String password = etPassword.getText().toString().trim();

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

                    db.collection("teachers").document(userId).set(teacherMap)
                            .addOnSuccessListener(unused -> {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Teacher added successfully", Toast.LENGTH_SHORT).show();
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
}
