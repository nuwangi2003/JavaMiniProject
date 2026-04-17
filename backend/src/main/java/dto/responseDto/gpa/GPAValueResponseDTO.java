package dto.responseDto.gpa;

public class GPAValueResponseDTO {
    private boolean success;
    private String message;
    private String studentId;
    private Integer academicYear;
    private Integer semester;
    private Double value;

    public GPAValueResponseDTO() {
    }

    public GPAValueResponseDTO(boolean success, String message, String studentId, Integer academicYear, Integer semester, Double value) {
        this.success = success;
        this.message = message;
        this.studentId = studentId;
        this.academicYear = academicYear;
        this.semester = semester;
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
