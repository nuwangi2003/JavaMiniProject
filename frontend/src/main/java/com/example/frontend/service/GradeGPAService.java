package com.example.frontend.service;

import com.example.frontend.network.ServerClient;
import com.example.frontend.session.SessionManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class GradeGPAService {

    private final ServerClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public GradeGPAService(ServerClient client) {
        this.client = client;
    }

    public JsonNode loadGPAReport(int academicYear, int academicLevel, String semester, String departmentId) throws Exception {
        ObjectNode root = mapper.createObjectNode();
        root.put("command", "GENERATE_GPA");
        root.put("token", SessionManager.getToken());

        ObjectNode data = root.putObject("data");
        data.put("academicYear", academicYear);
        data.put("academicLevel", academicLevel);
        data.put("semester", semester);
        data.put("departmentId", departmentId);

        String responseJson = client.sendRequest(mapper.writeValueAsString(root));
        return mapper.readTree(responseJson);
    }
    public boolean saveSemesterResults(int academicYear, int academicLevel, String semester,
                                       String departmentId, java.util.List<java.util.Map<String, String>> rows) {
        try {
            ObjectNode root = mapper.createObjectNode();
            root.put("command", "SAVE_SEMESTER_RESULTS");
            root.put("token", SessionManager.getToken());

            ObjectNode data = root.putObject("data");

            ObjectNode filter = data.putObject("filter");
            filter.put("academicYear", academicYear);
            filter.put("academicLevel", academicLevel);
            filter.put("semester", semester);
            filter.put("departmentId", departmentId);

            var rowsArray = data.putArray("rows");

            for (java.util.Map<String, String> row : rows) {
                ObjectNode r = rowsArray.addObject();
                r.put("studentId", row.getOrDefault("studentId", ""));
                r.put("regNo", row.getOrDefault("regNo", ""));
                r.put("studentName", row.getOrDefault("studentName", ""));
                r.put("totalCredits", Integer.parseInt(row.getOrDefault("totalCredits", "0")));
                r.put("sgpa", Double.parseDouble(row.getOrDefault("sgpa", "0")));
                r.put("cgpa", Double.parseDouble(row.getOrDefault("cgpa", "0")));
            }

            String responseJson = client.sendRequest(mapper.writeValueAsString(root));
            JsonNode response = mapper.readTree(responseJson);

            return response.path("success").asBoolean(false);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}