package command.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import dto.requestDto.result.GradeGPAFilterDTO;
import dto.responseDto.result.GradeGPAReportDTO;
import service.login.AuthService;
import service.result.GradeGPAService;

public class GenerateGradeGPACommand implements Command {

    private final GradeGPAService service;
    private final AuthService authService;
    private final ObjectMapper mapper = new ObjectMapper();

    public GenerateGradeGPACommand(GradeGPAService service, AuthService authService) {
        this.service = service;
        this.authService = authService;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        try {
            if (!authService.isTokenValid(context.getToken())) {
                context.getOutput().println("{\"success\":false}");
                return;
            }

            GradeGPAFilterDTO dto = mapper.convertValue(data, GradeGPAFilterDTO.class);
            GradeGPAReportDTO report = service.generate(dto);

            context.getOutput().println(mapper.writeValueAsString(report));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}