package command.eligibility;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;

import service.eligibility.CAEligibilityService;
import service.login.AuthService;

import java.util.HashMap;
import java.util.Map;

public class GetCAEligibilityCommand implements Command {

    private final ObjectMapper mapper = new ObjectMapper();
    private final CAEligibilityService service;
    private final AuthService authService;

    public GetCAEligibilityCommand(CAEligibilityService service, AuthService authService) {
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
                response.put("message", "Only lecturers can check CA eligibility.");
                context.getOutput().println(mapper.writeValueAsString(response));
                return;
            }

            Map<?, ?> map = mapper.convertValue(data, Map.class);
            String courseId = map.get("courseId").toString();

            response.put("success", true);
            response.put("message", "CA eligibility loaded successfully.");
            response.put("data", service.getCAEligibility(context.getUserId(), courseId));

            context.getOutput().println(mapper.writeValueAsString(response));

        } catch (Exception e) {
            e.printStackTrace();

            try {
                response.put("success", false);
                response.put("message", "Server error while checking CA eligibility.");
                context.getOutput().println(mapper.writeValueAsString(response));
            } catch (Exception ignored) {}
        }
    }
}