package com.example.frontend.service;

import com.example.frontend.model.FinalEligibilityRow;
import com.example.frontend.network.ServerClient;
import com.example.frontend.session.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collections;
import java.util.List;

public class FinalEligibilityService {

    private final ServerClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public FinalEligibilityService(ServerClient client) {
        this.client = client;
    }

    public List<FinalEligibilityRow> getFinalEligibility(String courseId) {
        try {
            ObjectNode root = mapper.createObjectNode();
            root.put("command", "GET_FINAL_ELIGIBILITY");
            root.put("token", SessionManager.getToken());

            ObjectNode data = root.putObject("data");
            data.put("courseId", courseId);

            String requestJson = mapper.writeValueAsString(root);
            String responseJson = client.sendRequest(requestJson);

            JsonNode responseNode = mapper.readTree(responseJson);

            if (!responseNode.path("success").asBoolean(false)) {
                return Collections.emptyList();
            }

            return mapper.readValue(
                    responseNode.path("data").toString(),
                    new TypeReference<List<FinalEligibilityRow>>() {}
            );

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}