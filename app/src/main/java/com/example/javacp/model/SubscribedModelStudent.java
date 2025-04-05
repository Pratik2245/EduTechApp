package com.example.javacp.model;

import com.google.firebase.firestore.PropertyName;

public class SubscribedModelStudent {
    private String courseTitle;
    private String teacherName;
    private String courseThumbnailUrl;
    private String courseId;
    private long subscribedAt;
    private String teacherId;
    private String userId;
    private String videoUrl;

    public SubscribedModelStudent() {}

    @PropertyName("courseTitle")
    public String getCourseTitle() { return courseTitle; }

    @PropertyName("teacherName")
    public String getTeacherName() { return teacherName; }

    @PropertyName("thumbnailUrl")
    public String getCourseThumbnailUrl() { return courseThumbnailUrl; }

    @PropertyName("thumbnailUrl")
    public void setCourseThumbnailUrl(String courseThumbnailUrl) { this.courseThumbnailUrl = courseThumbnailUrl; }

    @PropertyName("courseId")
    public String getCourseId() { return courseId; }

    @PropertyName("subscribedAt")
    public long getSubscribedAt() { return subscribedAt; }

    @PropertyName("teacherId")
    public String getTeacherId() { return teacherId; }

    @PropertyName("userId")
    public String getUserId() { return userId; }

    @PropertyName("videoUrl")
    public String getVideoUrl() { return videoUrl; }

    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
//    public void setCourseThumbnailUrl(String courseThumbnailUrl) { this.courseThumbnailUrl = courseThumbnailUrl; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public void setSubscribedAt(long subscribedAt) { this.subscribedAt = subscribedAt; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
}
