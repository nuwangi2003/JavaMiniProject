package com.example.frontend.model;

public class Student {

    private String userId;
    private String regNo;
    private String batch;
    private int academicLevel;
    private String departmentId;

    // Default constructor
    public Student() {
    }

    // Parameterized constructor
    public Student(String userId, String regNo, String batch, int academicLevel, String departmentId) {
        this.userId = userId;
        this.regNo = regNo;
        this.batch = batch;
        this.academicLevel = academicLevel;
        this.departmentId = departmentId;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public int getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(int academicLevel) {
        this.academicLevel = academicLevel;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

}
