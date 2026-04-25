package command.eligibility;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import service.eligibility.StudentEligibilityService;
import service.login.AuthService;

import java.util.HashMap;
import java.util.Map;

public class GetStudentEligibilityCommand implements Command {

    private final ObjectMapper mapper = new ObjectMapper();
    private final StudentEligibilityService service;
    private final AuthService authService;

    public GetStudentEligibilityCommand(StudentEligibilityService service, AuthService authService) {
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

            if (!"Lecturer".equalsIgnoreCase(context.getRole())) {
                response.put("success", false);
                response.put("message", "Only lecturers can check eligibility.");
                context.getOutput().println(mapper.writeValueAsString(response));
                return;
            }

            Map<?, ?> map = mapper.convertValue(data, Map.class);
            Object courseIdObj = map.get("courseId");

            if (courseIdObj == null || courseIdObj.toString().isBlank()) {
                response.put("success", false);
                response.put("message", "Course ID is required.");
                context.getOutput().println(mapper.writeValueAsString(response));
                return;
            }

            String courseId = courseIdObj.toString();

            response.put("success", true);
            response.put("message", "Eligibility loaded successfully.");
            response.put("data", service.getEligibilityByCourse(context.getUserId(), courseId));

            context.getOutput().println(mapper.writeValueAsString(response));

        } catch (Exception e) {
            e.printStackTrace();

            try {
                response.put("success", false);
                response.put("message", "Server error while loading eligibility.");
                context.getOutput().println(mapper.writeValueAsString(response));
            } catch (Exception ignored) {
            }
        }
    }
}