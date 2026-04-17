package command.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import command.repository.CommandJsonUtil;
import dto.requestDto.report.AcademicReportRequestDTO;
import dto.responseDto.report.AcademicReportResponseDTO;
import model.AcademicReportRow;
import service.report.AcademicReportService;

import java.util.List;

public class GetStudentFullAcademicReportCommand implements Command {

    private final AcademicReportService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public GetStudentFullAcademicReportCommand(AcademicReportService service) {
        this.service = service;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        try {
            AcademicReportRequestDTO request = mapper.convertValue(data, AcademicReportRequestDTO.class);
            if (request.getStudentId() == null || request.getStudentId().isBlank()) {
                CommandJsonUtil.writeError(mapper, context, "studentId is required");
                return;
            }

            List<AcademicReportRow> rows = service.getStudentFullAcademicReport(request.getStudentId());
            AcademicReportResponseDTO response = new AcademicReportResponseDTO(
                    rows != null && !rows.isEmpty(),
                    rows != null && !rows.isEmpty() ? "Student full academic report retrieved." : "No full academic report found.",
                    rows
            );

            context.getOutput().println(mapper.writeValueAsString(response));

        } catch (Exception e) {
            e.printStackTrace();
            CommandJsonUtil.writeError(mapper, context, "Server error");
        }
    }
}
