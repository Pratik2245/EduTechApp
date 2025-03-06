package com.example.javacp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class StudentFrag_5ProfileFragment extends Fragment {
    private TextView fullNameTextView, emailTextView,editTextRole,editTextBio,editTextPhone;
    private Button btnUpdateProfile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_student_frag_5_profile, container, false);
        fullNameTextView= rootView.findViewById(R.id.editTextFullName);
        emailTextView=rootView.findViewById(R.id.editTextEmail);
        editTextRole=rootView.findViewById(R.id.editTextRole);
        editTextBio=rootView.findViewById(R.id.editTextBio);
        editTextPhone=rootView.findViewById(R.id.editTextPhone);
        btnUpdateProfile=rootView.findViewById(R.id.btnUpdateProfile);

        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
//
        if(currentUser!=null){
            String userId=currentUser.getUid();
            FirebaseFirestore firestore=FirebaseFirestore.getInstance();
            firestore.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString("fullName");
                    String email1 = documentSnapshot.getString("email");
                    String role=documentSnapshot.getString("role");
                    String bio=documentSnapshot.getString("bio");
                    String phoneNumber=documentSnapshot.getString("phone");
                    fullNameTextView.setText(name);
                    editTextRole.setText(role);
                    emailTextView.setText(email1);
                    editTextBio.setText(bio!=null?bio:"");
                    editTextPhone.setText(phoneNumber!=null?phoneNumber:"");
                }

                else {
                    Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Log.e("ProfileFragment", "Error fetching user data", e);
                Toast.makeText(requireContext(), "Error in Profile Fragment", Toast.LENGTH_SHORT).show();
            });
        }

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert currentUser != null;
                String userId = currentUser.getUid();
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                // First, get the current document
                firestore.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retain existing values
                        String currentFullName = documentSnapshot.getString("fullName");
                        String currentEmail = documentSnapshot.getString("email");
                        String currentRole = documentSnapshot.getString("role");

                        // Get updated values
                        String updatedPhone = editTextPhone.getText().toString().trim();
                        String updatedBio = editTextBio.getText().toString().trim();

                        // Validate Phone Number
                        if (updatedPhone.isEmpty()) {
                            editTextPhone.setError("Phone number is required");
                            editTextPhone.requestFocus();
                            return;
                        }
                        if (updatedPhone.length() != 10 || !updatedPhone.matches("\\d+")) {
                            editTextPhone.setError("Enter a valid 10-digit phone number");
                            editTextPhone.requestFocus();
                            return;
                        }
                        // Create a map with all fields to be updated
                        Map<String, Object> updatedData = new HashMap<>();
                        updatedData.put("fullName", currentFullName);
                        updatedData.put("email", currentEmail);
                        updatedData.put("role", currentRole);
                        updatedData.put("phone", updatedPhone);
                        updatedData.put("bio", updatedBio);

                        // Update the document with the merged data
                        firestore.collection("users").document(userId)
                                .set(updatedData) // Use set with complete data
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(requireContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Log.e("ProfileFragment", "Error fetching user data", e);
                    Toast.makeText(requireContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();
                });
            }
        });
        return rootView;
    }
}