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

public class GetBatchFullAcademicReportCommand implements Command {

    private final AcademicReportService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public GetBatchFullAcademicReportCommand(AcademicReportService service) {
        this.service = service;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        try {
            AcademicReportRequestDTO request = mapper.convertValue(data, AcademicReportRequestDTO.class);
            if (request.getBatch() == null || request.getBatch().isBlank()) {
                CommandJsonUtil.writeError(mapper, context, "batch is required");
                return;
            }

            List<AcademicReportRow> rows = service.getBatchFullAcademicReport(request.getBatch());
            AcademicReportResponseDTO response = new AcademicReportResponseDTO(
                    rows != null && !rows.isEmpty(),
                    rows != null && !rows.isEmpty() ? "Batch full academic report retrieved." : "No batch full academic report found.",
                    rows
            );

            context.getOutput().println(mapper.writeValueAsString(response));

        } catch (Exception e) {
            e.printStackTrace();
            CommandJsonUtil.writeError(mapper, context, "Server error");
        }
    }
}
