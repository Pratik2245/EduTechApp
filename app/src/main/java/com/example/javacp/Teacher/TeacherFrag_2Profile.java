package com.example.javacp.Teacher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.javacp.databinding.FragmentTeacherFrag2ProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class TeacherFrag_2Profile extends Fragment {

    FragmentTeacherFrag2ProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTeacherFrag2ProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Fetching user profile data
        loadTeacherData(currentUser);

        // Setting listener to update button
        binding.btnUpdate.setOnClickListener(v -> {
            if (currentUser != null) {
                String userId = currentUser.getUid();
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                firestore.collection("users").document(userId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String updatedBio = binding.etBio.getText().toString().trim();
                                if (updatedBio.isEmpty()) {
                                    binding.etBio.setError("Bio is Required");
                                    binding.etBio.requestFocus();
                                    return;
                                }


                                // Update only the bio field
                                Map<String, Object> updatedData = new HashMap<>();
                                updatedData.put("bio", updatedBio);

                                // Merge update to Firestore
                                firestore.collection("users").document(userId)
                                        .set(updatedData, SetOptions.merge()) // Merges instead of overwriting
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(requireContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                            // Reload the Fragment
                                            requireActivity().getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .detach(TeacherFrag_2Profile.this)
                                                    .attach(TeacherFrag_2Profile.this)
                                                    .commit();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("ProfileFragmentTeacher", "Error updating profile", e);
                                            Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                                        });

                            } else {
                                Toast.makeText(getContext(), "User Data not Found", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("ProfileFragmentTeacher", "Error fetching user data", e);
                            Toast.makeText(requireContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        return view;
    }

    private void loadTeacherData(FirebaseUser currentUser) {
        if (currentUser != null) {
            // Convert the user id into string format
            String userId = currentUser.getUid();
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("fullName");
                            String email = documentSnapshot.getString("email");
                            String experience = documentSnapshot.getString("experience");
                            String phone = documentSnapshot.getString("phone");
                            String qualifications = documentSnapshot.getString("qualifications");
                            String role = documentSnapshot.getString("role");
                            String specialization = documentSnapshot.getString("specialization");
                            String bio = documentSnapshot.getString("bio"); // Fetch bio

                            binding.etFullName.setText(name);
                            binding.etEmail.setText(email);
                            binding.etRole.setText(role);
                            binding.etQualifications.setText(qualifications);
                            binding.etPhone.setText(phone);
                            binding.etSpecialization.setText(specialization);
                            binding.etExperience.setText(experience);
                            binding.etBio.setText(bio); // Set bio
                        } else {
                            Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ProfileFragmentTeacher", "Error loading user data", e);
                        Toast.makeText(requireContext(), "Error loading user data", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
