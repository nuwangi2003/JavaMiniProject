package com.example.frontend.model;

public class AttendanceSessionOption {
    private Integer sessionId;
    private String courseId;
    private String sessionDate;
    private String type;

    public AttendanceSessionOption(Integer sessionId, String courseId, String sessionDate, String type) {
        this.sessionId = sessionId;
        this.courseId = courseId;
        this.sessionDate = sessionDate;
        this.type = type;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "Session " + sessionId + " - " + courseId + " - " + sessionDate + " (" + type + ")";
    }
}
