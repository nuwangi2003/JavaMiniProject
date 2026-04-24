package dto.requestDto.result;

public class GenerateCourseResultReqDTO {

    private String courseId;
    private int academicYear;
    private int academicLevel;
    private String semester;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public int getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(int academicLevel) {
        this.academicLevel = academicLevel;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}