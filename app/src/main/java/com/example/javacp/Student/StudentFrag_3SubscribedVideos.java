package com.example.javacp.Student;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.javacp.Adapter.SubscribedCourseAdapter;
import com.example.javacp.R;
import com.example.javacp.model.SubscribedModelStudent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentFrag_3SubscribedVideos extends Fragment {
    private RecyclerView recyclerView;
    private SubscribedCourseAdapter adapter;
    private List<SubscribedModelStudent> courseList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_student_frag_3_subscribed_videos, container, false);

//        Toolbar toolbar = view.findViewById(R.id.toolbarSubscribed);
//        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        recyclerView = view.findViewById(R.id.recyclerViewSubscribed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseList = new ArrayList<>();
        adapter = new SubscribedCourseAdapter(getContext(), courseList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchSubscribedCourses();

        return view;
    }
    @SuppressLint("NotifyDataSetChanged")
    private void fetchSubscribedCourses() {
        String currentUserId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        db.collection("subscribed_courses")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    courseList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        SubscribedModelStudent course = doc.toObject(SubscribedModelStudent.class);
                        courseList.add(course);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to load courses", Toast.LENGTH_SHORT).show());
    }
}