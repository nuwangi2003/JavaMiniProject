package com.example.frontend.service;

import com.example.frontend.model.CAEligibilityRow;
import com.example.frontend.network.ServerClient;
import com.example.frontend.session.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collections;
import java.util.List;

public class CAEligibilityService {

    private final ServerClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public CAEligibilityService(ServerClient client) {
        this.client = client;
    }

    public List<CAEligibilityRow> getCAEligibility(String courseId) {
        try {
            ObjectNode root = mapper.createObjectNode();
            root.put("command", "GET_CA_ELIGIBILITY");
            root.put("token", SessionManager.getToken());

            ObjectNode data = root.putObject("data");
            data.put("courseId", courseId);

            String responseJson = client.sendRequest(mapper.writeValueAsString(root));
            JsonNode responseNode = mapper.readTree(responseJson);

            if (!responseNode.path("success").asBoolean(false)) {
                return Collections.emptyList();
            }

            return mapper.readValue(
                    responseNode.path("data").toString(),
                    new TypeReference<List<CAEligibilityRow>>() {}
            );

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}