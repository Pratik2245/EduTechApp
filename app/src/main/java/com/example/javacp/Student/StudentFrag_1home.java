package com.example.javacp.Student;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

    private EditText edtSearch;
    private RecyclerView recyclerView;
    private CourseAdapterStudent adapter;
    private List<CourseModelStudent> courseList;
    private List<CourseModelStudent> fullCourseList; // For searching
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_frag_1home, container, false);

        edtSearch = view.findViewById(R.id.edt_search);
        recyclerView = view.findViewById(R.id.rv_courses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        courseList = new ArrayList<>();
        fullCourseList = new ArrayList<>();
        adapter = new CourseAdapterStudent(getContext(), courseList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadCourses();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCourses(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void loadCourses() {
        db.collection("courses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    courseList.clear();
                    fullCourseList.clear();

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String imageUrl = document.getString("thumbnailUrl");
                        String title = document.getString("title");
                        String description = document.getString("description");
                        String price = document.getString("price");
                        String videoUrl = document.getString("videoUrl");
                        String teacherId = document.getString("teacherUid");
                        String teacherName = document.getString("teacherName");

                        if (price == null || price.isEmpty()) {
                            price = "0";
                        }

                        String courseId = document.getId();

                        CourseModelStudent course = new CourseModelStudent(
                                courseId,
                                imageUrl,
                                title,
                                description,
                                price,
                                videoUrl,
                                teacherId,
                                teacherName
                        );

                        courseList.add(course);
                        fullCourseList.add(course);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", "Failed to fetch courses", e));
    }

    private void filterCourses(String query) {
        List<CourseModelStudent> filteredList = new ArrayList<>();
        for (CourseModelStudent course : fullCourseList) {
            if (course.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    course.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                    course.getTeacherName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(course);
            }
        }
        adapter.filterList(filteredList);
    }
}
