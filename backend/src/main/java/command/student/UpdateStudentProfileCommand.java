package command.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import dto.requestDto.student.UpdateStudentProfileReqDTO;
import service.login.AuthService;
import service.student.StudentService;

public class UpdateStudentProfileCommand implements Command {

    private final StudentService studentService;
    private final AuthService authService;
    private final ObjectMapper mapper = new ObjectMapper();

    public UpdateStudentProfileCommand(StudentService studentService, AuthService authService) {
        this.studentService = studentService;
        this.authService = authService;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        try {
            String token = context.getToken();

            if (token == null || !authService.isTokenValid(token)) {
                context.getOutput().println("{\"success\":false,\"message\":\"Unauthorized\"}");
                return;
            }

            UpdateStudentProfileReqDTO dto =
                    mapper.convertValue(data, UpdateStudentProfileReqDTO.class);

            boolean success = studentService.updateStudentProfile(dto);

            if (success) {
                context.getOutput().println(
                        "{\"success\":true,\"message\":\"Profile updated successfully\"}"
                );
            } else {
                context.getOutput().println(
                        "{\"success\":false,\"message\":\"Profile update failed\"}"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            context.getOutput().println("{\"success\":false,\"message\":\"Server error\"}");
        }
    }
}