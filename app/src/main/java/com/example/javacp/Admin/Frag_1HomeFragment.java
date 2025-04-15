package com.example.javacp.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.javacp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class Frag_1HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private TextView studentCountTextView, teacherCountTextView, courseCountTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frag_1_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get references to the TextViews
        studentCountTextView = view.findViewById(R.id.studentCount);
        teacherCountTextView = view.findViewById(R.id.teacherCount);
        courseCountTextView = view.findViewById(R.id.courseCount);

        // Real-time listener on 'users' collection to filter roles
        db.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if (error != null || value == null) return;

                        int studentCount = 0;
                        int teacherCount = 0;

                        for (DocumentSnapshot doc : value) {
                            String role = doc.getString("role");
                            if (role != null) {
                                if (role.equalsIgnoreCase("student")) {
                                    studentCount++;
                                } else if (role.equalsIgnoreCase("teacher")) {
                                    teacherCount++;
                                }
                            }
                        }

                        studentCountTextView.setText("Students: " + studentCount);
                        teacherCountTextView.setText("Teachers: " + teacherCount);
                    }
                });

        // Real-time listener for courses (assuming separate collection)
        db.collection("courses")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if (error != null || value == null) return;
                        int courseCount = value.size();
                        courseCountTextView.setText("Courses: " + courseCount);
                    }
                });
    }
}
