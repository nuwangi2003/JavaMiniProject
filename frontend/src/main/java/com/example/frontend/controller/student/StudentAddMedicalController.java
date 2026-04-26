package com.example.frontend.controller.student;

import com.example.frontend.controller.admin.LoginController;
import com.example.frontend.model.AttendanceCourseOption;
import com.example.frontend.model.Medical;
import com.example.frontend.service.AttendanceService;
import com.example.frontend.service.MedicalService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentAddMedicalController {
    private static final int MAX_MEDICAL_COPY_LENGTH = 255;

    @FXML private Label studentNameLabel;
    @FXML private Label headerRegNoLabel;
    @FXML private Label regNoLabel;
    @FXML private Label todayLabel;
    @FXML private ComboBox<String> courseCombo;
    @FXML private ComboBox<String> examTypeCombo;
    @FXML private DatePicker submittedDatePicker;
    @FXML private TextField medicalFileField;
    @FXML private Label statusLabel;

    private final MedicalService medicalService = new MedicalService(LoginController.client);
    private final AttendanceService attendanceService = new AttendanceService(LoginController.client);
    private final Map<String, String> courseDisplayToId = new HashMap<>();

    @FXML
    public void initialize() {
        studentNameLabel.setText(LoginController.username == null || LoginController.username.isBlank()
                ? "Student"
                : LoginController.username);
        String regNo = LoginController.reNo == null || LoginController.reNo.isBlank()
                ? "Reg No not found"
                : LoginController.reNo;
        headerRegNoLabel.setText(regNo);
        regNoLabel.setText(regNo);
        todayLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));

        examTypeCombo.setItems(FXCollections.observableArrayList("Attendance", "Mid", "Final"));
        examTypeCombo.getSelectionModel().select("Attendance");

        courseCombo.setEditable(false);
        submittedDatePicker.setValue(LocalDate.now());

        showStatus("Submit a medical only for your own attendance or exam issue.", StatusType.INFO);
        loadCourseOptions();
    }

    @FXML
    private void submitMedical() {
        String selectedCourse = courseCombo.getValue();
        String courseId = selectedCourse == null ? null : courseDisplayToId.get(selectedCourse);
        String examType = examTypeCombo.getValue();
        LocalDate submittedDate = submittedDatePicker.getValue();
        String medicalCopy = medicalFileField.getText() == null ? "" : medicalFileField.getText().trim();

        if (courseId == null || courseId.isBlank()) {
            showStatus("Select a course before submitting.", StatusType.ERROR);
            return;
        }

        if (submittedDate == null) {
            showStatus("Select the submitted date.", StatusType.ERROR);
            return;
        }

        if (medicalCopy.isBlank()) {
            showStatus("Choose your medical file before submitting.", StatusType.ERROR);
            return;
        }

        File selectedFile = new File(medicalCopy);
        if (!selectedFile.exists() || !selectedFile.isFile()) {
            showStatus("Selected medical file was not found on this computer.", StatusType.ERROR);
            return;
        }

        if (medicalCopy.length() > MAX_MEDICAL_COPY_LENGTH) {
            showStatus("File path is too long for database storage. Please choose a shorter location.", StatusType.ERROR);
            return;
        }

        Medical added = medicalService.addMedicalForCurrentStudent(
                courseId,
                examType,
                submittedDate.toString(),
                medicalCopy
        );

        if (added == null) {
            showStatus(medicalService.getLastMessage(), StatusType.ERROR);
            return;
        }

        showStatus("Medical record #" + added.getMedicalId() + " submitted successfully.", StatusType.SUCCESS);
        medicalFileField.clear();
        examTypeCombo.getSelectionModel().select("Attendance");
        submittedDatePicker.setValue(LocalDate.now());
    }

    @FXML
    private void chooseMedicalFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Medical File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF and Images", "*.pdf", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp", "*.webp"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp", "*.webp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) statusLabel.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile == null) {
            return;
        }

        String selectedPath = selectedFile.getAbsolutePath();
        if (selectedPath.length() > MAX_MEDICAL_COPY_LENGTH) {
            showStatus("File path is too long for database storage. Please choose a shorter location.", StatusType.ERROR);
            return;
        }

        medicalFileField.setText(selectedPath);
        showStatus("Medical file selected. The local file path will be saved with this record.", StatusType.SUCCESS);
    }

    @FXML
    private void clearForm() {
        if (!courseCombo.getItems().isEmpty()) {
            courseCombo.getSelectionModel().selectFirst();
        } else {
            courseCombo.getSelectionModel().clearSelection();
        }
        examTypeCombo.getSelectionModel().select("Attendance");
        submittedDatePicker.setValue(LocalDate.now());
        medicalFileField.clear();
        showStatus("", StatusType.INFO);
    }

    @FXML
    private void openViewMedical() {
        loadView("/view/student/StudentViewMedical.fxml");
    }

    @FXML
    private void backToDashboard() {
        loadView("/view/student/studentDashboard.fxml");
    }

    private void loadCourseOptions() {
        List<AttendanceCourseOption> courses = attendanceService.getMyMedicalEligibleCourseIds();
        courseDisplayToId.clear();
        courseCombo.getItems().clear();

        for (AttendanceCourseOption option : courses) {
            String display = option.getDisplayText();
            if (display != null && !display.isBlank()) {
                courseDisplayToId.put(display, option.getCourseId());
                courseCombo.getItems().add(display);
            }
        }

        if (!courseCombo.getItems().isEmpty()) {
            courseCombo.getSelectionModel().selectFirst();
        } else {
            showStatus(attendanceService.getLastMessage().isBlank()
                    ? "No eligible courses found for medical submission."
                    : attendanceService.getLastMessage(), StatusType.ERROR);
        }
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
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Cannot open view");
            alert.setContentText(path);
            alert.showAndWait();
        }
    }

    private void showStatus(String message, StatusType type) {
        statusLabel.setText(message == null ? "" : message);

        String color = switch (type) {
            case SUCCESS -> "#2f9e44";
            case ERROR -> "#d94841";
            case INFO -> "#5c7cfa";
        };

        statusLabel.setStyle(
                "-fx-text-fill: " + color + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;"
        );
    }

    private enum StatusType {
        SUCCESS, ERROR, INFO
    }
}
