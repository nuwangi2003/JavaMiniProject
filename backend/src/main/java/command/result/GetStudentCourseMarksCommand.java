package command.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import service.login.AuthService;
import service.result.StudentCourseMarksService;

import java.util.HashMap;
import java.util.Map;

public class GetStudentCourseMarksCommand implements Command {

    private final ObjectMapper mapper = new ObjectMapper();
    private final StudentCourseMarksService service;
    private final AuthService authService;

    public GetStudentCourseMarksCommand(StudentCourseMarksService service, AuthService authService) {
        this.service = service;
        this.authService = authService;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        Map<String, Object> response = new HashMap<>();

        try {
            String token = context.getToken();

            if (token == null || !authService.isTokenValid(token)) {
                response.put("success", false);
                response.put("message", "Invalid or expired token.");
                context.getOutput().println(mapper.writeValueAsString(response));
                return;
            }

            if (!"Lecturer".equalsIgnoreCase(context.getRole())
                    && !"Admin".equalsIgnoreCase(context.getRole())
                    && !"Dean".equalsIgnoreCase(context.getRole())) {

                response.put("success", false);
                response.put("message", "You are not allowed to view student marks.");
                context.getOutput().println(mapper.writeValueAsString(response));
                return;
            }

            response.put("success", true);
            response.put("message", "Student course marks loaded successfully.");
            response.put("data", service.getAllStudentCourseMarks());

            context.getOutput().println(mapper.writeValueAsString(response));

        } catch (Exception e) {
            e.printStackTrace();

            try {
                response.put("success", false);
                response.put("message", "Server error while loading student course marks.");
                context.getOutput().println(mapper.writeValueAsString(response));
            } catch (Exception ignored) {}
        }
    }
}