package com.example.javacp.model;

public class CourseModelAdmin {
    private String courseName;
    private String courseDescription;
    private String thumbnailUrl;
    private String price;

    public CourseModelAdmin(String courseName, String courseDescription, String thumbnailUrl, String price) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.thumbnailUrl = thumbnailUrl;
        this.price = price;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getPrice() {
        return price;
    }
}
