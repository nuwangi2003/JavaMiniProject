package dto.responseDto.result;

import java.util.LinkedHashMap;
import java.util.Map;

public class GradeGPARowDTO {

    private String studentId;
    private String regNo;
    private String studentName;

    private int totalCredits;
    private double sgpa;
    private double cgpa;

    private Map<String, String> courseGrades = new LinkedHashMap<>();

    public GradeGPARowDTO() {
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public double getSgpa() {
        return sgpa;
    }

    public void setSgpa(double sgpa) {
        this.sgpa = sgpa;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public Map<String, String> getCourseGrades() {
        return courseGrades;
    }

    public void setCourseGrades(Map<String, String> courseGrades) {
        this.courseGrades = courseGrades;
    }
}