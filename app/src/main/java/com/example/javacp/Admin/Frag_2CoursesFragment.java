package com.example.javacp.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javacp.Adapter.CourseAdapterAdmin;
import com.example.javacp.R;
import com.example.javacp.model.CourseModelAdmin;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Frag_2CoursesFragment extends Fragment {

    private RecyclerView recyclerView;
    private CourseAdapterAdmin adapter;
    private List<CourseModelAdmin> courseList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_frag_2_courses, container, false);

        recyclerView = view.findViewById(R.id.rv_admin_courses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        courseList = new ArrayList<>();
        adapter = new CourseAdapterAdmin(getContext(), courseList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadCourses(); // Fetch courses from Firestore

        return view;
    }

    private void loadCourses() {
        db.collection("courses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    courseList.clear();  // Clear the list before adding new data
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String imageUrl = document.getString("thumbnailUrl");
                        String title = document.getString("title");
                        String description = document.getString("description");
                        String price = document.getString("price");

                        // Create a CourseModelAdmin object with additional fields
                        CourseModelAdmin course = new CourseModelAdmin(title, description, imageUrl, price);
                        courseList.add(course);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", "Failed to fetch courses", e));
    }
}
