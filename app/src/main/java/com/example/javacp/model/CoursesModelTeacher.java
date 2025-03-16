package com.example.javacp.model;

public class CoursesModelTeacher {
    private String title, description, price, thumbnailUrl, videoUrl, teacherUid;

    public CoursesModelTeacher() {} // Required for Firestore

    public CoursesModelTeacher(String title, String description, String price, String thumbnailUrl, String videoUrl, String teacherUid) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.teacherUid = teacherUid;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getVideoUrl() { return videoUrl; }
}
