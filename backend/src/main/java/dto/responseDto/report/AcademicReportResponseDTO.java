package dto.responseDto.report;

import model.AcademicReportRow;

import java.util.List;

public class AcademicReportResponseDTO {
    private boolean success;
    private String message;
    private List<AcademicReportRow> data;

    public AcademicReportResponseDTO() {
    }

    public AcademicReportResponseDTO(boolean success, String message, List<AcademicReportRow> data) {
        this.success = success;
        this.message = message;
        this.data = data;
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

    public List<AcademicReportRow> getData() {
        return data;
    }

    public void setData(List<AcademicReportRow> data) {
        this.data = data;
    }
}
