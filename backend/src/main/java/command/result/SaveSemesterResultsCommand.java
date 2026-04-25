package command.result;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import dto.requestDto.result.GradeGPAFilterDTO;
import dto.responseDto.result.GradeGPARowDTO;
import service.login.AuthService;
import service.result.GradeGPAService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveSemesterResultsCommand implements Command {

    private final ObjectMapper mapper = new ObjectMapper();
    private final GradeGPAService service;
    private final AuthService authService;

    public SaveSemesterResultsCommand(GradeGPAService service, AuthService authService) {
        this.service = service;
        this.authService = authService;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (context.getToken() == null || !authService.isTokenValid(context.getToken())) {
                response.put("success", false);
                response.put("message", "Invalid token.");
                context.getOutput().println(mapper.writeValueAsString(response));
                return;
            }

            Map<?, ?> map = mapper.convertValue(data, Map.class);

            GradeGPAFilterDTO filter =
                    mapper.convertValue(map.get("filter"), GradeGPAFilterDTO.class);

            List<GradeGPARowDTO> rows =
                    mapper.convertValue(map.get("rows"), new TypeReference<List<GradeGPARowDTO>>() {});

            boolean saved = service.saveSemesterResults(filter, rows);

            response.put("success", saved);
            response.put("message", saved ? "Semester results saved." : "Failed to save.");

            context.getOutput().println(mapper.writeValueAsString(response));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}