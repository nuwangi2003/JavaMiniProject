package command.gpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import command.repository.CommandJsonUtil;
import dto.requestDto.gpa.GPAQueryRequestDTO;
import dto.responseDto.gpa.StudentGPAReportResponseDTO;
import model.GPAReport;
import service.gpa.GPAService;

import java.util.List;

public class GetStudentGPAReportCommand implements Command {

    private final GPAService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public GetStudentGPAReportCommand(GPAService service) {
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

            List<GPAReport> report = service.getStudentGPAReport(request.getStudentId());
            StudentGPAReportResponseDTO response = new StudentGPAReportResponseDTO(
                    report != null && !report.isEmpty(),
                    report != null && !report.isEmpty() ? "Student GPA report retrieved." : "No GPA records found.",
                    report
            );

            context.getOutput().println(mapper.writeValueAsString(response));

        } catch (Exception e) {
            e.printStackTrace();
            CommandJsonUtil.writeError(mapper, context, "Server error");
        }
    }
}
