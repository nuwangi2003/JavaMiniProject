package dto.responseDto.result;

import java.util.ArrayList;
import java.util.List;

public class GradeGPAReportDTO {

    private List<String> courses = new ArrayList<>();
    private List<GradeGPARowDTO> rows = new ArrayList<>();

    public GradeGPAReportDTO() {
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public List<GradeGPARowDTO> getRows() {
        return rows;
    }

    public void setRows(List<GradeGPARowDTO> rows) {
        this.rows = rows;
    }
}