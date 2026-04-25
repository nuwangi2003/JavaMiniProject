package com.example.frontend.controller.student;

import com.example.frontend.controller.admin.LoginController;
import com.example.frontend.model.Medical;
import com.example.frontend.service.MedicalService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentViewMedicalController {

    @FXML private Label studentNameLabel;
    @FXML private Label regNoLabel;
    @FXML private Label summaryLabel;
    @FXML private Label statusLabel;
    @FXML private Label todayLabel;
    @FXML private TableView<Medical> medicalTable;
    @FXML private TableColumn<Medical, Integer> idColumn;
    @FXML private TableColumn<Medical, String> courseColumn;
    @FXML private TableColumn<Medical, String> examTypeColumn;
    @FXML private TableColumn<Medical, String> submittedDateColumn;
    @FXML private TableColumn<Medical, String> statusColumn;
    @FXML private TableColumn<Medical, String> copyColumn;

    private final MedicalService medicalService = new MedicalService(LoginController.client);

    @FXML
    public void initialize() {
        studentNameLabel.setText(LoginController.username == null || LoginController.username.isBlank()
                ? "Student"
                : LoginController.username);
        regNoLabel.setText(LoginController.reNo == null || LoginController.reNo.isBlank()
                ? "Reg No not found"
                : LoginController.reNo);
        todayLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("medicalId"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        examTypeColumn.setCellValueFactory(new PropertyValueFactory<>("examType"));
        submittedDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateSubmitted"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        copyColumn.setCellValueFactory(new PropertyValueFactory<>("medicalCopy"));

        medicalTable.setPlaceholder(new Label("No medical records uploaded yet."));

        refreshRecords();
    }

    @FXML
    private void refreshRecords() {
        List<Medical> records = medicalService.getMyMedicalRecords();
        medicalTable.setItems(FXCollections.observableArrayList(records));

        if (records.isEmpty()) {
            summaryLabel.setText("0 records");
            statusLabel.setText(medicalService.getLastMessage().isBlank()
                    ? "No medical records found."
                    : medicalService.getLastMessage());
            statusLabel.setStyle("-fx-text-fill: #d94841; -fx-font-size: 12px; -fx-font-weight: bold;");
            return;
        }

        long pendingCount = records.stream().filter(m -> "Pending".equalsIgnoreCase(m.getStatus())).count();
        long approvedCount = records.stream().filter(m -> "Approved".equalsIgnoreCase(m.getStatus())).count();
        long rejectedCount = records.stream().filter(m -> "Rejected".equalsIgnoreCase(m.getStatus())).count();

        summaryLabel.setText(records.size() + " records | Pending: " + pendingCount
                + " | Approved: " + approvedCount + " | Rejected: " + rejectedCount);
        statusLabel.setText("Showing only your uploaded medical records.");
        statusLabel.setStyle("-fx-text-fill: #2f9e44; -fx-font-size: 12px; -fx-font-weight: bold;");
    }

    @FXML
    private void openAddMedical() {
        loadView("/view/student/StudentAddMedical.fxml");
    }

    @FXML
    private void backToDashboard() {
        loadView("/view/student/studentDashboard.fxml");
    }

    private void loadView(String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = (Stage) medicalTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Cannot open view");
            alert.setContentText(path);
            alert.showAndWait();
        }
    }
}
