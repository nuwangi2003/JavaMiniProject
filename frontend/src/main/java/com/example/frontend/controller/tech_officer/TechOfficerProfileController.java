package com.example.frontend.controller.tech_officer;

import com.example.frontend.service.AuthService;
import com.example.frontend.session.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.example.frontend.controller.admin.LoginController;


public class TechOfficerProfileController {

    @FXML
    private Label usernameValueLabel;
    @FXML
    private Label userIdValueLabel;
    @FXML
    private Label roleValueLabel;
    @FXML
    private Label tokenHintLabel;

    @FXML
    public void initialize() {
        String username = LoginController.username == null ? "-" : LoginController.username;
        String userId = SessionManager.getUserId() == null ? "-" : SessionManager.getUserId();
        String role = SessionManager.getRole() == null ? "-" : SessionManager.getRole();
        String token = SessionManager.getToken() == null ? "" : SessionManager.getToken();

        usernameValueLabel.setText(username);
        userIdValueLabel.setText(userId);
        roleValueLabel.setText(role);
        tokenHintLabel.setText(token.isBlank()
                ? "No active token"
                : "Token active (" + Math.min(token.length(), 20) + "+ chars)");
    }

    @FXML
    private void backToDashboard() {
        loadView("/view/techOfficerDashboard.fxml");
    }

    @FXML
    private void logout() {
        try {
            AuthService authService = new AuthService(LoginController.client);
            boolean success = authService.logout(SessionManager.getToken());
            if (success) {
                SessionManager.clear();
            }
        } catch (Exception ignored) {
        }
        loadLogin();
    }

    private void loadView(String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = (Stage) usernameValueLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLogin() {
        try {
            Stage current = (Stage) usernameValueLabel.getScene().getWindow();
            current.close();
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
