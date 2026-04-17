package model;

public class BatchGPAReportRow {
    private String studentId;
    private String regNo;
    private String studentName;
    private Double sgpa;
    private Double cgpa;

    public BatchGPAReportRow() {
    }

    public BatchGPAReportRow(String studentId, String regNo, String studentName, Double sgpa, Double cgpa) {
        this.studentId = studentId;
        this.regNo = regNo;
        this.studentName = studentName;
        this.sgpa = sgpa;
        this.cgpa = cgpa;
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

    public Double getSgpa() {
        return sgpa;
    }

    public void setSgpa(Double sgpa) {
        this.sgpa = sgpa;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }
}
