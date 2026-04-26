package com.example.frontend.controller.attendance;

import com.example.frontend.controller.admin.LoginController;
import com.example.frontend.model.AllAttendanceRow;
import com.example.frontend.service.AttendanceService;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AllAttendanceController {

    @FXML private ComboBox<String> viewTypeComboBox;
    @FXML private TableView<AllAttendanceRow> attendanceTable;
    @FXML private TableColumn<AllAttendanceRow, Integer> attendanceIdColumn;
    @FXML private TableColumn<AllAttendanceRow, String> regNoColumn;
    @FXML private TableColumn<AllAttendanceRow, String> studentNameColumn;
    @FXML private TableColumn<AllAttendanceRow, String> departmentColumn;
    @FXML private TableColumn<AllAttendanceRow, String> batchColumn;
    @FXML private TableColumn<AllAttendanceRow, String> courseIdColumn;
    @FXML private TableColumn<AllAttendanceRow, String> sessionDateColumn;
    @FXML private TableColumn<AllAttendanceRow, String> sessionTypeColumn;
    @FXML private TableColumn<AllAttendanceRow, String> statusColumn;
    @FXML private TableColumn<AllAttendanceRow, String> hoursAttendedColumn;
    @FXML private Label statusLabel;
    @FXML private Label totalRecordsLabel;

    private final AttendanceService attendanceService = new AttendanceService(LoginController.client);

    @FXML
    public void initialize() {
        viewTypeComboBox.getItems().addAll("Combined", "Theory", "Practical");
        viewTypeComboBox.getSelectionModel().selectFirst();

        attendanceIdColumn.setCellValueFactory(new PropertyValueFactory<>("attendanceId"));
        regNoColumn.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("departmentDisplay"));
        batchColumn.setCellValueFactory(new PropertyValueFactory<>("batch"));
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        sessionDateColumn.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        sessionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionType"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        hoursAttendedColumn.setCellValueFactory(new PropertyValueFactory<>("hoursAttendedDisplay"));

        loadAllAttendance();
    }

    @FXML
    private void loadAllAttendance() {
        String viewType = viewTypeComboBox.getValue() == null ? "Combined" : viewTypeComboBox.getValue();
        JsonNode response = attendanceService.getAllAttendance(viewType);
        JsonNode dataNode = response == null ? null : response.path("data");

        if (response == null || !response.path("success").asBoolean(false)) {
            attendanceTable.setItems(FXCollections.observableArrayList());
            totalRecordsLabel.setText("0 records");
            showStatus(attendanceService.getLastMessage(), StatusType.ERROR);
            return;
        }

        var rows = FXCollections.<AllAttendanceRow>observableArrayList();
        if (dataNode != null && dataNode.isArray()) {
            for (JsonNode item : dataNode) {
                AllAttendanceRow row = new AllAttendanceRow();
                row.setAttendanceId(item.path("attendanceId").asInt());
                row.setStudentId(item.path("studentId").asText(""));
                row.setRegNo(item.path("regNo").asText("-"));
                row.setStudentName(item.path("studentName").asText("-"));
                row.setDepartmentId(item.path("departmentId").asText(""));
                row.setDepartmentName(item.path("departmentName").asText(""));
                row.setBatch(item.path("batch").asText("-"));
                row.setSessionId(item.path("sessionId").asInt());
                row.setCourseId(item.path("courseId").asText("-"));
                row.setSessionDate(item.path("sessionDate").asText("-"));
                row.setSessionType(item.path("sessionType").asText("-"));
                row.setStatus(item.path("status").asText("-"));
                row.setHoursAttended(item.path("hoursAttended").asDouble(0.0));
                rows.add(row);
            }
        }

        attendanceTable.setItems(rows);
        totalRecordsLabel.setText(rows.size() + (rows.size() == 1 ? " record" : " records"));

        if (rows.isEmpty()) {
            showStatus("No attendance records available for the selected view.", StatusType.INFO);
        } else {
            showStatus("All attendance loaded successfully.", StatusType.SUCCESS);
        }
    }

    @FXML
    private void backToDashboard() {
        loadView("/view/techofficer/techOfficerDashboard.fxml");
    }

    private void loadView(String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot open view");
            alert.setContentText(path);
            alert.showAndWait();
        }
    }

    private enum StatusType {
        SUCCESS, ERROR, INFO
    }

    private void showStatus(String message, StatusType type) {
        statusLabel.setText(message == null || message.isBlank() ? "-" : message);

        String color = switch (type) {
            case SUCCESS -> "#4cba52";
            case ERROR -> "#e85d5d";
            case INFO -> "#8fa3b8";
        };

        statusLabel.setStyle(
                "-fx-text-fill: " + color + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;"
        );
    }
}
