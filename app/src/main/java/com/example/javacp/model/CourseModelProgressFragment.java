package com.example.javacp.model;

public class CourseModelProgressFragment {
        public String courseTitle, teacherName, thumbnail;
        public float progressPercent;

        public CourseModelProgressFragment() {} // Needed for Firebase

        public CourseModelProgressFragment(String courseTitle, String teacherName, String thumbnail, float progressPercent) {
            this.courseTitle = courseTitle;
            this.teacherName = teacherName;
            this.thumbnail = thumbnail;
            this.progressPercent = progressPercent;
        }
    }

