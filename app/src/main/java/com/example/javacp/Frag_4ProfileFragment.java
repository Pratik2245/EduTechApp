package com.example.javacp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class Frag_4ProfileFragment extends Fragment {
    private TextView fullNameTextView, emailTextView,editTextRole;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_frag_4_profile, container, false);

        fullNameTextView= rootView.findViewById(R.id.editTextFullName);
        emailTextView=rootView.findViewById(R.id.editTextEmail);
        editTextRole=rootView.findViewById(R.id.editTextRole);
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
//
        if(currentUser!=null){
            String userId=currentUser.getUid();
            Toast.makeText(getContext(), "userid="+userId, Toast.LENGTH_SHORT).show();
            FirebaseFirestore firestore=FirebaseFirestore.getInstance();
            firestore.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString("fullName");
                    String email1 = documentSnapshot.getString("email");
                    String role=documentSnapshot.getString("role");
                        fullNameTextView.setText(name);
                        editTextRole.setText(role);
                        emailTextView.setText(email1);
                }
                else {
                    Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Log.e("ProfileFragment", "Error fetching user data", e);
                Toast.makeText(requireContext(), "Error in Profile Fragment", Toast.LENGTH_SHORT).show();
            });
        }

        return rootView;
    }
}