package com.example.frontend.service;

import com.example.frontend.dto.UserRequestDTO;
import com.example.frontend.network.ServerClient;
import com.example.frontend.session.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;


public class UserService {
    private final ServerClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public UserService(ServerClient client) {
        this.client = client;
    }

    public boolean createUser(UserRequestDTO dto) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("command", "CreateUser");
            request.put("data", dto);
            request.put("token", SessionManager.getToken());

            String json = mapper.writeValueAsString(request);

            String response = client.sendRequest(json); // send to backend
            System.out.println("create user : " + response);

            // parse response from backend
            Map<String, Object> map = mapper.readValue(response, Map.class);

            // backend returns {"success": true/false, "message": "..."}
            return Boolean.TRUE.equals(map.get("success"));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
