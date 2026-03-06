package command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.FinalMarksService;

public class GetStudentFinalMarksCommand implements Command {

    FinalMarksService service = new FinalMarksService();

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void execute(JsonNode data, ClientContext context) {

        try{

            String studentId = data.get("studentId").asText();

            var marks = service.getStudentMarks(studentId);

            String json = mapper.writeValueAsString(marks);

            context.getOutput().println(json);

        }catch(Exception e){

            context.getOutput().println("{\"success\":false}");
        }
    }
}