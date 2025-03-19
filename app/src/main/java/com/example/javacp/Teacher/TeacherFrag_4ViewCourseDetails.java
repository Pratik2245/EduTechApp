package com.example.javacp.Teacher;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.javacp.Adapter.CourseAdapterTeacher;
import com.example.javacp.R;
import com.example.javacp.model.CoursesModelTeacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeacherFrag_4ViewCourseDetails extends Fragment {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private List<CoursesModelTeacher> courseList;
    private CourseAdapterTeacher courseAdapterTeacher;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_frag_4_view_course_details, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        courseList = new ArrayList<>();
        courseAdapterTeacher = new CourseAdapterTeacher(getContext(), courseList);
        recyclerView.setAdapter(courseAdapterTeacher);

        loadTeacherCourses();
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadTeacherCourses() {
        String teacherUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firestore.collection("courses")
                .whereEqualTo("teacherUid", teacherUid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        courseList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            CoursesModelTeacher course = doc.toObject(CoursesModelTeacher.class);
                            courseList.add(course);
                        }
                        courseAdapterTeacher.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Failed to load courses", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}