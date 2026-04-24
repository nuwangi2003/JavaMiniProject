package command.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import dto.requestDto.result.GenerateCourseResultReqDTO;
import service.login.AuthService;
import service.result.CourseResultGeneratorService;

import java.util.HashMap;
import java.util.Map;

public class GenerateCourseResultCommand implements Command {

    private final ObjectMapper mapper = new ObjectMapper();
    private final CourseResultGeneratorService service;
    private final AuthService authService;

    public GenerateCourseResultCommand(CourseResultGeneratorService service, AuthService authService) {
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
                response.put("message", "You are not allowed to generate course results.");
                context.getOutput().println(mapper.writeValueAsString(response));
                return;
            }

            GenerateCourseResultReqDTO dto =
                    mapper.convertValue(data, GenerateCourseResultReqDTO.class);

            int count = service.generateCourseResults(dto);

            response.put("success", count > 0);
            response.put("message", count + " course results generated.");
            response.put("count", count);

            context.getOutput().println(mapper.writeValueAsString(response));

        } catch (Exception e) {
            e.printStackTrace();

            try {
                response.put("success", false);
                response.put("message", "Server error while generating course results.");
                context.getOutput().println(mapper.writeValueAsString(response));
            } catch (Exception ignored) {
            }
        }
    }
}