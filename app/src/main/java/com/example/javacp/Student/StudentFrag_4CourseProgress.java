package com.example.javacp.Student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.javacp.Adapter.CourseProgressAdapter;
import com.example.javacp.R;
import com.example.javacp.model.CourseModelProgressFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class StudentFrag_4CourseProgress extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_student_frag_4_course_progress, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCourseProgress);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<CourseModelProgressFragment> courseList = new ArrayList<>();
        CourseProgressAdapter adapter = new CourseProgressAdapter(courseList);
        recyclerView.setAdapter(adapter);

        String studentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("course_progress")
                .whereEqualTo("studentId", studentId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    courseList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        CourseModelProgressFragment course = doc.toObject(CourseModelProgressFragment.class);
                        courseList.add(course);
                    }
                    adapter.notifyDataSetChanged();
                });

        return view;
    }
}