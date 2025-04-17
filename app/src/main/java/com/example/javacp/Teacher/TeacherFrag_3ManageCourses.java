
package com.example.javacp.Teacher;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.javacp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class TeacherFrag_3ManageCourses extends Fragment {

    private TextView courseCountTextView;
    private FirebaseFirestore db;
    private String teacherUid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_frag_3_manage_courses, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        courseCountTextView = view.findViewById(R.id.courseCount);

        // Real-time course listener
        teacherUid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current logged-in teacher UID

        // Listen to only current teacher's courses
        db.collection("courses")
                .whereEqualTo("teacherUid", teacherUid)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;

                    courseCountTextView.setText("Courses: " + value.size());
                });

    }
}