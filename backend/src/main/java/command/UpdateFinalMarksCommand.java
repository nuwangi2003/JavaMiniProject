package command;

import com.fasterxml.jackson.databind.JsonNode;
import service.FinalMarksService;

public class UpdateFinalMarksCommand implements Command {

    FinalMarksService service = new FinalMarksService();

    @Override
    public void execute(JsonNode data, ClientContext context) {

        try{

            String studentId = data.get("studentId").asText();
            String courseId = data.get("courseId").asText();
            int year = data.get("academicYear").asInt();
            String semester = data.get("semester").asText();
            double marks = data.get("marks").asDouble();

            boolean result = service.updateFinalMarks(studentId,courseId,year,semester,marks);

            context.getOutput().println(
            "{\"success\":"+result+"}");

        }catch(Exception e){

            context.getOutput().println(
            "{\"success\":false}");
        }
    }
}