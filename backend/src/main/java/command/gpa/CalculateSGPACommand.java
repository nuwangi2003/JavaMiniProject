package command.gpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import command.repository.CommandJsonUtil;
import dto.requestDto.gpa.GPAQueryRequestDTO;
import dto.responseDto.gpa.GPAValueResponseDTO;
import service.gpa.GPAService;

public class CalculateSGPACommand implements Command {

    private final GPAService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public CalculateSGPACommand(GPAService service) {
        this.service = service;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        try {
            GPAQueryRequestDTO request = mapper.convertValue(data, GPAQueryRequestDTO.class);
            if (request.getStudentId() == null || request.getAcademicYear() == null || request.getSemester() == null) {
                CommandJsonUtil.writeError(mapper, context, "studentId, academicYear and semester are required");
                return;
            }

            Double value = service.calculateSGPA(request.getStudentId(), request.getAcademicYear(), request.getSemester());
            GPAValueResponseDTO response = new GPAValueResponseDTO(
                    value != null,
                    value != null ? "SGPA calculated." : "No results found for SGPA calculation.",
                    request.getStudentId(),
                    request.getAcademicYear(),
                    request.getSemester(),
                    value
            );
            context.getOutput().println(mapper.writeValueAsString(response));

        } catch (Exception e) {
            e.printStackTrace();
            CommandJsonUtil.writeError(mapper, context, "Server error");
        }
    }
}
