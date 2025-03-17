package com.example.javacp.Admin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.javacp.databinding.FragmentFrag4ProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;

public class Frag_4ProfileFragment extends Fragment {
    FragmentFrag4ProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFrag4ProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Load user data
        loadUserData(currentUser);

        // Update button listener
        binding.btnUpdateProfile.setOnClickListener(v -> {
            if (currentUser != null) {
                String userId = currentUser.getUid();
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                firestore.collection("users").document(userId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String updatedBio = binding.editTextBio.getText().toString().trim();
                                String updatedPhone = binding.editTextPhone.getText().toString().trim();

                                // Validate Bio
                                if (updatedBio.isEmpty()) {
                                    binding.editTextBio.setError("Bio is required");
                                    binding.editTextBio.requestFocus();
                                    return;
                                }

                                // Validate Phone Number
                                if (updatedPhone.isEmpty()) {
                                    binding.editTextPhone.setError("Phone number is required");
                                    binding.editTextPhone.requestFocus();
                                    return;
                                }
                                if (updatedPhone.length() != 10 || !updatedPhone.matches("\\d+")) {
                                    binding.editTextPhone.setError("Enter a valid 10-digit phone number");
                                    binding.editTextPhone.requestFocus();
                                    return;
                                }

                                // Update Firestore with merged fields
                                Map<String, Object> updatedData = new HashMap<>();
                                updatedData.put("bio", updatedBio);
                                updatedData.put("phone", updatedPhone);

                                firestore.collection("users").document(userId)
                                        .set(updatedData, SetOptions.merge())
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(requireContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                            requireActivity().getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .detach(Frag_4ProfileFragment.this)
                                                    .attach(Frag_4ProfileFragment.this)
                                                    .commit();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("ProfileFragment", "Error updating profile", e);
                                            Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                                        });

                            } else {
                                Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("ProfileFragment", "Error fetching user data", e);
                            Toast.makeText(requireContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        return view;
    }

    private void loadUserData(FirebaseUser currentUser) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("fullName");
                            String email = documentSnapshot.getString("email");
                            String role = documentSnapshot.getString("role");
                            String bio = documentSnapshot.getString("bio");
                            String phone = documentSnapshot.getString("phone");

                            binding.editTextFullName.setText(name);
                            binding.editTextEmail.setText(email);
                            binding.editTextRole.setText(role);
                            binding.editTextBio.setText(bio != null ? bio : "");
                            binding.editTextPhone.setText(phone != null ? phone : "");
                        } else {
                            Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ProfileFragment", "Error loading user data", e);
                        Toast.makeText(requireContext(), "Error loading user data", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}