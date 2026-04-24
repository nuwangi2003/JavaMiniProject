package com.example.frontend.service;

import com.example.frontend.model.StudentCourseMarksRow;
import com.example.frontend.network.ServerClient;
import com.example.frontend.session.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collections;
import java.util.List;

public class StudentCourseMarksService {

    private final ServerClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public StudentCourseMarksService(ServerClient client) {
        this.client = client;
    }

    public List<StudentCourseMarksRow> getStudentCourseMarks() {
        try {
            ObjectNode root = mapper.createObjectNode();
            root.put("command", "GET_STUDENT_COURSE_MARKS");
            root.put("token", SessionManager.getToken());

            root.putObject("data");

            String responseJson = client.sendRequest(mapper.writeValueAsString(root));
            JsonNode node = mapper.readTree(responseJson);

            if (!node.path("success").asBoolean(false)) {
                return Collections.emptyList();
            }

            return mapper.readValue(
                    node.path("data").toString(),
                    new TypeReference<List<StudentCourseMarksRow>>() {}
            );

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}