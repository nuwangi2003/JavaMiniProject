package command.undergraduate;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.repository.ClientContext;
import command.repository.Command;
import command.repository.CommandJsonUtil;
import dto.responseDto.report.MyDataResponseDTO;
import service.report.UndergraduateViewService;

public class GetAllNoticesCommand implements Command {

    private final UndergraduateViewService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public GetAllNoticesCommand(UndergraduateViewService service) {
        this.service = service;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        try {
            MyDataResponseDTO response = new MyDataResponseDTO(
                    true,
                    "Notices retrieved.",
                    service.getAllNotices()
            );
            context.getOutput().println(mapper.writeValueAsString(response));
        } catch (Exception e) {
            e.printStackTrace();
            CommandJsonUtil.writeError(mapper, context, "Server error");
        }
    }
}
