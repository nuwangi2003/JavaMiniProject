package com.example.frontend.controller.attendance;

import com.example.frontend.controller.admin.LoginController;
import com.example.frontend.model.Attendance;
import com.example.frontend.model.AttendanceStudentOption;
import com.example.frontend.service.AttendanceService;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAttendanceController {

    @FXML
    private TextField attendanceIdField;
    @FXML
    private Label attendanceIdValue;
    @FXML
    private Label studentIdValue;
    @FXML
    private Label sessionIdValue;
    @FXML
    private Label statusValue;
    @FXML
    private Label hoursValue;
    @FXML
    private Label resultLabel;

    private final AttendanceService attendanceService = new AttendanceService(LoginController.client);
    private final Map<String, String> regNoToUserId = new HashMap<>();

    @FXML
    public void initialize() {
        loadStudentRegNoMap();
        clearValues();
        showResult("", ResultType.INFO);
    }

    @FXML
    private void findAttendance() {
        try {
            String input = attendanceIdField.getText() == null ? "" : attendanceIdField.getText().trim();
            if (input.isEmpty()) {
                showError("Please enter a student reg no.");
                return;
            }

            String studentUserId = resolveStudentUserId(input);
            if (studentUserId == null) {
                showError("Invalid student reg no. Use a valid registration number.");
                return;
            }

            JsonNode response = attendanceService.getStudentAttendance(studentUserId, "Combined");
            JsonNode dataNode = response == null ? null : response.path("data");

            if (response == null || !response.path("success").asBoolean(false) || dataNode == null || !dataNode.isArray() || dataNode.size() == 0) {
                showResult("No attendance record found for this student.", ResultType.ERROR);
                clearValues();
                return;
            }

            JsonNode latestRecord = dataNode.get(0);
            Attendance attendance = new Attendance();
            attendance.setAttendanceId(latestRecord.path("attendanceId").asInt());
            attendance.setStudentId(latestRecord.path("studentId").asText(""));
            attendance.setSessionId(latestRecord.path("sessionId").asInt());
            attendance.setStatus(latestRecord.path("status").asText(""));
            attendance.setHoursAttended(latestRecord.path("hoursAttended").asDouble());

            attendanceIdValue.setText(String.valueOf(attendance.getAttendanceId()));
            studentIdValue.setText(attendance.getStudentId() == null ? "-" : attendance.getStudentId());
            sessionIdValue.setText(String.valueOf(attendance.getSessionId()));
            statusValue.setText(attendance.getStatus() == null ? "-" : attendance.getStatus());
            hoursValue.setText(String.valueOf(attendance.getHoursAttended()));

            showResult("Latest attendance loaded successfully for student reg no.", ResultType.SUCCESS);

        } catch (Exception ex) {
            showError("Unexpected error: " + ex.getMessage());
        }
    }

    @FXML
    private void goUpdateDelete() {
        loadView("/view/techofficer/AttendanceManagement.fxml");
    }

    @FXML
    private void backToDashboard() {
        loadView("/view/techofficer/techOfficerDashboard.fxml");
    }

    private void clearValues() {
        attendanceIdValue.setText("-");
        studentIdValue.setText("-");
        sessionIdValue.setText("-");
        statusValue.setText("-");
        hoursValue.setText("-");
    }

    private void loadView(String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = (Stage) resultLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            showError("Cannot load view: " + path);
        }
    }

    private void showError(String message) {
        showResult(message, ResultType.ERROR);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadStudentRegNoMap() {
        regNoToUserId.clear();
        List<AttendanceStudentOption> options = attendanceService.getStudentOptions();
        for (AttendanceStudentOption option : options) {
            if (option.getRegNo() != null && option.getUserId() != null) {
                regNoToUserId.put(option.getRegNo().trim().toLowerCase(), option.getUserId().trim());
            }
        }
    }

    private String resolveStudentUserId(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }
        String trimmed = input.trim();
        if (trimmed.toUpperCase().startsWith("U")) {
            return trimmed;
        }
        return regNoToUserId.get(trimmed.toLowerCase());
    }

    private enum ResultType {
        SUCCESS, ERROR, INFO
    }

    private void showResult(String message, ResultType type) {
        resultLabel.setText(message);

        String color = switch (type) {
            case SUCCESS -> "#4cba52";
            case ERROR -> "#e85d5d";
            case INFO -> "#8fa3b8";
        };

        resultLabel.setStyle(
                "-fx-text-fill: " + color + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;"
        );
    }
}
