package com.example.frontend.model;

public class StudentCourseMarksRow {

    private String studentId;
    private String regNo;
    private String studentName;
    private String departmentName;
    private int academicLevel;
    private String semester;

    private String courseId;
    private String courseCode;
    private String courseName;

    private double attendancePercentage;
    private double caPercentage;
    private double finalExamMarks;

    private String medicalStatus;
    private String resultStatus;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public int getAcademicLevel() { return academicLevel; }
    public void setAcademicLevel(int academicLevel) { this.academicLevel = academicLevel; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

    public double getCaPercentage() { return caPercentage; }
    public void setCaPercentage(double caPercentage) { this.caPercentage = caPercentage; }

    public double getFinalExamMarks() { return finalExamMarks; }
    public void setFinalExamMarks(double finalExamMarks) { this.finalExamMarks = finalExamMarks; }

    public String getMedicalStatus() { return medicalStatus; }
    public void setMedicalStatus(String medicalStatus) { this.medicalStatus = medicalStatus; }

    public String getResultStatus() { return resultStatus; }
    public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }
}