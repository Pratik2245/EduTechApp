package com.example.javacp.model;

public class CoursesModelTeacher {
    private String title, description, price, thumbnailUrl, videoUrl,teacherName;
    private String teacherId;
    private String courseId;

    public CoursesModelTeacher() {} // Required for Firestore

    public CoursesModelTeacher(String title, String description, String price, String thumbnailUrl, String videoUrl, String teacherUid, String teacherName) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.teacherId = teacherUid;
        this.teacherName = teacherName;
    }
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getVideoUrl() { return videoUrl; }
    public String getTeacherName() { return teacherName; } // ✅ Getter
}
