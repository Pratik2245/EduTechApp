package com.example.javacp.model;

public class CourseModelStudent {
    private String courseId;
    private String thumbnailUrl;
    private String title;
    private String description;
    private String price;

    private String videoUrl;
    private String teacherId;
    private String teacherName;

    // Full Constructor
    public CourseModelStudent(String courseId, String thumbnailUrl, String title, String description, String price,
                              String videoUrl, String teacherId, String teacherName) {
        this.courseId = courseId;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.description = description;
        this.price = price;
        this.videoUrl = videoUrl;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    // Short Constructor (optional, can remove if unused)
    public CourseModelStudent(String courseId, String thumbnailUrl, String title, String description, String price) {
        this.courseId = courseId;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    // Getters
    public String getCourseId() {
        return courseId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    // Setters
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
