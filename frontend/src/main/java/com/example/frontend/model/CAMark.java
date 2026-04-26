package com.example.frontend.model;

public class CAMark {
    private Integer markId;
    private String studentId;
    private String studentRegNo;
    private Integer assessmentTypeId;
    private String courseId;
    private String assessmentName;
    private Double assessmentWeight;
    private Double marks;

    public Integer getMarkId() {
        return markId;
    }

    public void setMarkId(Integer markId) {
        this.markId = markId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentRegNo() {
        return studentRegNo;
    }

    public void setStudentRegNo(String studentRegNo) {
        this.studentRegNo = studentRegNo;
    }

    public Integer getAssessmentTypeId() {
        return assessmentTypeId;
    }

    public void setAssessmentTypeId(Integer assessmentTypeId) {
        this.assessmentTypeId = assessmentTypeId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public Double getAssessmentWeight() {
        return assessmentWeight;
    }

    public void setAssessmentWeight(Double assessmentWeight) {
        this.assessmentWeight = assessmentWeight;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }
}
