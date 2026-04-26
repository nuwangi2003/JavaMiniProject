package dto.requestDto.attendance;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAllAttendanceRequestDTO {
    @JsonProperty("view_type")
    private String viewType;

    public GetAllAttendanceRequestDTO() {
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }
}
