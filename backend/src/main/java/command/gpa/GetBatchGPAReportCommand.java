package command.gpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import command.repository.CommandJsonUtil;
import dto.requestDto.gpa.GPAQueryRequestDTO;
import dto.responseDto.gpa.BatchGPAReportResponseDTO;
import model.BatchGPAReportRow;
import service.gpa.GPAService;

import java.util.List;

public class GetBatchGPAReportCommand implements Command {

    private final GPAService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public GetBatchGPAReportCommand(GPAService service) {
        this.service = service;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        try {
            GPAQueryRequestDTO request = mapper.convertValue(data, GPAQueryRequestDTO.class);
            if (request.getBatch() == null || request.getAcademicYear() == null || request.getSemester() == null) {
                CommandJsonUtil.writeError(mapper, context, "batch, academicYear and semester are required");
                return;
            }

            List<BatchGPAReportRow> report = service.getBatchGPAReport(request.getBatch(), request.getAcademicYear(), request.getSemester());
            BatchGPAReportResponseDTO response = new BatchGPAReportResponseDTO(
                    report != null && !report.isEmpty(),
                    report != null && !report.isEmpty() ? "Batch GPA report retrieved." : "No batch GPA records found.",
                    report
            );

            context.getOutput().println(mapper.writeValueAsString(response));

        } catch (Exception e) {
            e.printStackTrace();
            CommandJsonUtil.writeError(mapper, context, "Server error");
        }
    }
}
