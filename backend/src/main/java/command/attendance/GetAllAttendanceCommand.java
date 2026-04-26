package command.attendance;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import dto.requestDto.attendance.GetAllAttendanceRequestDTO;
import dto.responseDto.attendance.AttendanceResponseDTO;
import service.attendance.AttendanceService;

public class GetAllAttendanceCommand implements Command {
    private final AttendanceService attendanceService;
    private final ObjectMapper mapper = new ObjectMapper();

    public GetAllAttendanceCommand(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        try {
            if (!isAllowedRole(context.getRole())) {
                context.getOutput().println("{\"success\":false,\"message\":\"Forbidden: insufficient role\"}");
                return;
            }

            GetAllAttendanceRequestDTO request = mapper.convertValue(data, GetAllAttendanceRequestDTO.class);
            AttendanceResponseDTO response = new AttendanceResponseDTO(
                    true,
                    "All attendance loaded",
                    attendanceService.getAllAttendance(request == null ? null : request.getViewType())
            );
            context.getOutput().println(mapper.writeValueAsString(response));
        } catch (Exception e) {
            e.printStackTrace();
            context.getOutput().println("{\"success\":false,\"message\":\"Server error\"}");
        }
    }

    private boolean isAllowedRole(String role) {
        return "Tech_Officer".equalsIgnoreCase(role)
                || "Admin".equalsIgnoreCase(role)
                || "Dean".equalsIgnoreCase(role)
                || "Lecturer".equalsIgnoreCase(role);
    }
}
