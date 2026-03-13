package com.example.frontend.controller;

import com.example.frontend.model.User;
import com.example.frontend.network.ServerClient;
import com.example.frontend.service.AuthService;
import com.example.frontend.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final ServerClient client = new ServerClient();

    @FXML
    public void initialize() {
        usernameField.setOnAction(actionEvent -> passwordField.requestFocus() );
        passwordField.setOnAction(event -> login());
    }

    @FXML
    public void login() {
        try {
            client.connect();
            AuthService authService = new AuthService(client);

            User user = authService.login(
                    usernameField.getText(),
                    passwordField.getText()
            );

            if(user != null){
                // Store token & role in session
                SessionManager.setToken(user.getToken());
                SessionManager.setRole(user.getRole());

                System.out.println("Login success!");
                System.out.println("Token: " + user.getToken());
                System.out.println("Role: " + user.getRole());

                // Load different pages based on role
                switch(user.getRole()){
                    case "Student":
                        System.out.println("Load Student page");
                        break;
                    case "Lecturer":
                        System.out.println("Load Lecturer page");
                        break;
                    case "Admin":
                        System.out.println("Load Admin page");
                        break;
                }

            } else {
                System.out.println("Login failed");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}