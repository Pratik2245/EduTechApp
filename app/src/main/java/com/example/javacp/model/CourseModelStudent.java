package com.example.javacp.model;

public class CourseModelStudent {
    private String thumbnailUrl;  // URL of the course image
    private String title;
    private String description;
    private String instructor;
    private String price;

    // Constructor
    public CourseModelStudent(String thumbnailUrl, String title, String description, String price) {
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    // Getters
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

    // Setters (if needed)
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
}
