package command;

import com.fasterxml.jackson.databind.JsonNode;
import service.FinalMarksService;

/*
Command = Endpoint
This class handles UploadFinalMarks request
*/

public class UploadFinalMarksCommand implements Command {

    FinalMarksService service = new FinalMarksService();

    @Override
    public void execute(JsonNode data, ClientContext context) {

        try{

            // read data from JSON request
            String studentId = data.get("studentId").asText();
            String courseId = data.get("courseId").asText();
            int year = data.get("academicYear").asInt();
            int level = data.get("academicLevel").asInt();
            String semester = data.get("semester").asText();
            double marks = data.get("marks").asDouble();

            boolean result = service.uploadFinalMarks(studentId,courseId,year,level,semester,marks);

            if(result){

                context.getOutput().println(
                "{\"success\":true,\"message\":\"Final marks uploaded\"}");

            }else{

                context.getOutput().println(
                "{\"success\":false,\"message\":\"Upload failed\"}");
            }

        }catch(Exception e){

            context.getOutput().println(
            "{\"success\":false,\"message\":\"Server error\"}");
        }
    }
}