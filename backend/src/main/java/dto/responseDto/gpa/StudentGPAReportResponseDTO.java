package dto.responseDto.gpa;

import model.GPAReport;

import java.util.List;

public class StudentGPAReportResponseDTO {
    private boolean success;
    private String message;
    private List<GPAReport> data;

    public StudentGPAReportResponseDTO() {
    }

    public StudentGPAReportResponseDTO(boolean success, String message, List<GPAReport> data) {
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

    public List<GPAReport> getData() {
        return data;
    }

    public void setData(List<GPAReport> data) {
        this.data = data;
    }
}
