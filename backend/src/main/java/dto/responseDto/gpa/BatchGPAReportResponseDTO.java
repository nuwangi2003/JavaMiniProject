package dto.responseDto.gpa;

import model.BatchGPAReportRow;

import java.util.List;

public class BatchGPAReportResponseDTO {
    private boolean success;
    private String message;
    private List<BatchGPAReportRow> data;

    public BatchGPAReportResponseDTO() {
    }

    public BatchGPAReportResponseDTO(boolean success, String message, List<BatchGPAReportRow> data) {
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

    public List<BatchGPAReportRow> getData() {
        return data;
    }

    public void setData(List<BatchGPAReportRow> data) {
        this.data = data;
    }
}
