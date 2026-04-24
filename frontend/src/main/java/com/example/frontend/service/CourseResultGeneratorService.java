package com.example.frontend.service;

import com.example.frontend.network.ServerClient;
import com.example.frontend.session.SessionManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CourseResultGeneratorService {

    private final ServerClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public CourseResultGeneratorService(ServerClient client) {
        this.client = client;
    }

    public boolean generate(String courseId, int academicYear, int academicLevel, String semester) {
        try {
            ObjectNode root = mapper.createObjectNode();
            root.put("command", "GENERATE_COURSE_RESULT");
            root.put("token", SessionManager.getToken());

            ObjectNode data = root.putObject("data");
            data.put("courseId", courseId);
            data.put("academicYear", academicYear);
            data.put("academicLevel", academicLevel);
            data.put("semester", semester);

            String response = client.sendRequest(mapper.writeValueAsString(root));
            JsonNode node = mapper.readTree(response);

            return node.path("success").asBoolean(false);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}