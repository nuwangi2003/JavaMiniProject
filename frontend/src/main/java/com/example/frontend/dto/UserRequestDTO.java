package com.example.frontend.dto;

public class UserRequestDTO {
    private String username;
    private String email;
    private String password;
    private String contactNumber;
    private String profilePicture;
    private String role;

    // Student-specific
    private String regNo;
    private String batch;
    private int academicLevel;
    private String departmentId;

    // Lecturer-specific
    private String specialization;
    private String designation;

    // Tech Officer-specific
    private String techDepartmentId;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getTechDepartmentId() {
        return techDepartmentId;
    }

    public void setTechDepartmentId(String techDepartmentId) {
        this.techDepartmentId = techDepartmentId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public int getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(int academicLevel) {
        this.academicLevel = academicLevel;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}