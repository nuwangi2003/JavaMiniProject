package model;

public class GPAReport {
    private String studentId;
    private Integer academicYear;
    private Integer semester;
    private Double sgpa;
    private Double cgpa;

    public GPAReport() {
    }

    public GPAReport(String studentId, Integer academicYear, Integer semester, Double sgpa, Double cgpa) {
        this.studentId = studentId;
        this.academicYear = academicYear;
        this.semester = semester;
        this.sgpa = sgpa;
        this.cgpa = cgpa;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
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
