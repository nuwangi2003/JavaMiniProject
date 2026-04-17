package command.gpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import command.repository.CommandJsonUtil;
import dto.requestDto.gpa.GPAQueryRequestDTO;
import dto.responseDto.gpa.GPAValueResponseDTO;
import service.gpa.GPAService;

public class CalculateCGPACommand implements Command {

    private final GPAService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public CalculateCGPACommand(GPAService service) {
        this.service = service;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        try {
            GPAQueryRequestDTO request = mapper.convertValue(data, GPAQueryRequestDTO.class);
            if (request.getStudentId() == null) {
                CommandJsonUtil.writeError(mapper, context, "studentId is required");
                return;
            }

            Double value = service.calculateCGPA(request.getStudentId());
            GPAValueResponseDTO response = new GPAValueResponseDTO(
                    value != null,
                    value != null ? "CGPA calculated." : "No results found for CGPA calculation.",
                    request.getStudentId(),
                    null,
                    null,
                    value
            );
            context.getOutput().println(mapper.writeValueAsString(response));

        } catch (Exception e) {
            e.printStackTrace();
            CommandJsonUtil.writeError(mapper, context, "Server error");
        }
    }
}
