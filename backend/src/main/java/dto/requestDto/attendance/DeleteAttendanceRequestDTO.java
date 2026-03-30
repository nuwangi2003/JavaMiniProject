package dto.requestDto.attendance;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteAttendanceRequestDTO {

    @JsonProperty("attendance_id")
    private Integer attendanceId;

    public DeleteAttendanceRequestDTO() {
    }

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }
}
