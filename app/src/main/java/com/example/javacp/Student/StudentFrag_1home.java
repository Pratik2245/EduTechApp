package com.example.javacp.Student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javacp.Adapter.CourseAdapterStudent;
import com.example.javacp.R;
import com.example.javacp.model.CourseModelStudent;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StudentFrag_1home extends Fragment {
    private RecyclerView recyclerView;
    private CourseAdapterStudent adapter;
    private List<CourseModelStudent> courseList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_frag_1home, container, false);
        recyclerView = view.findViewById(R.id.rv_courses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        courseList = new ArrayList<>();
        adapter = new CourseAdapterStudent(getContext(), courseList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadCourses();
        return view;
    }

    private void loadCourses() {
        db.collection("courses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    courseList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String imageUrl = document.getString("thumbnailUrl");
                        String title = document.getString("title");
                        String description = document.getString("description");
                        String price = document.getString("price");

                        if (price == null || price.isEmpty()) {
                            price = "0";  // Default price if missing
                        }

                        CourseModelStudent course = new CourseModelStudent(imageUrl, title, description, price);
                        courseList.add(course);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", "Failed to fetch courses", e));
    }
}
