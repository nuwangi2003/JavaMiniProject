package com.example.frontend.service;

import com.example.frontend.model.User;
import com.example.frontend.network.ServerClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthService {

    private final ServerClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public AuthService(ServerClient client){
        this.client = client;
    }

    public User login(String username, String password) throws Exception {

        String loginJson = String.format(
                "{\"command\":\"LOGIN\",\"data\":{\"username\":\"%s\",\"password\":\"%s\"}}",
                username, password
        );


        String response = client.sendRequest(loginJson);


        JsonNode node = mapper.readTree(response);

        if(node.get("success").asBoolean()){

            String role = node.has("role") ? node.get("role").asText() : "";
            String token = node.has("token") ? node.get("token").asText() : "";
            String userId = node.has("user_id") ? node.get("user_id").asText() : "";


            User user = new User(userId, username, "", role);
            user.setToken(token);
            return user;
        }

        return null;
    }
}