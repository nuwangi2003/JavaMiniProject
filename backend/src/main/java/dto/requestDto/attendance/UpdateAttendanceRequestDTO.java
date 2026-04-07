package dto.requestDto.attendance;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateAttendanceRequestDTO {

    @JsonProperty("attendance_id")
    private Integer attendanceId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("hours_attended")
    private Double hoursAttended;

    public UpdateAttendanceRequestDTO() {
    }

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getHoursAttended() {
        return hoursAttended;
    }

    public void setHoursAttended(Double hoursAttended) {
        this.hoursAttended = hoursAttended;
    }
}
