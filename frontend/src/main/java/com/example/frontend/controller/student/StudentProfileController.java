package com.example.frontend.controller.student;

import com.example.frontend.model.Student;
import com.example.frontend.network.ServerClient;
import com.example.frontend.service.StudentService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class StudentProfileController {

    @FXML private TextField userIdField;
    @FXML private TextField usernameField;
    @FXML private TextField regNoField;
    @FXML private TextField batchField;
    @FXML private TextField academicLevelField;
    @FXML private TextField departmentIdField;

    @FXML private TextField emailField;
    @FXML private TextField contactNumberField;
    @FXML private TextField profilePictureField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private Label statusLabel;
    @FXML private Button saveBtn;

    private Student currentStudent;
    private final StudentService studentService = new StudentService(ServerClient.getInstance());

    public void setStudent(Student student) {
        this.currentStudent = student;
        loadStudentData();
    }

    private void loadStudentData() {
        if (currentStudent == null) return;

        userIdField.setText(currentStudent.getUserId());
        usernameField.setText(currentStudent.getUsername());
        regNoField.setText(currentStudent.getRegNo());
        batchField.setText(currentStudent.getBatch());
        academicLevelField.setText(String.valueOf(currentStudent.getAcademicLevel()));
        departmentIdField.setText(currentStudent.getDepartmentId());

        emailField.setText(currentStudent.getEmail() != null ? currentStudent.getEmail() : "");
        contactNumberField.setText(currentStudent.getContactNumber() != null ? currentStudent.getContactNumber() : "");
        profilePictureField.setText(currentStudent.getProfilePicture() != null ? currentStudent.getProfilePicture() : "");

        userIdField.setEditable(false);
        usernameField.setEditable(false);
        regNoField.setEditable(false);
        batchField.setEditable(false);
        academicLevelField.setEditable(false);
        departmentIdField.setEditable(false);
    }

    @FXML
    private void browseProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(profilePictureField.getScene().getWindow());
        if (file != null) {
            profilePictureField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void saveProfile() {
        if (currentStudent == null) {
            showStatus("Student data not loaded.", false);
            return;
        }

        String email = emailField.getText() != null ? emailField.getText().trim() : "";
        String contactNumber = contactNumberField.getText() != null ? contactNumberField.getText().trim() : "";
        String profilePicture = profilePictureField.getText() != null ? profilePictureField.getText().trim() : "";
        String password = passwordField.getText() != null ? passwordField.getText().trim() : "";
        String confirmPassword = confirmPasswordField.getText() != null ? confirmPasswordField.getText().trim() : "";

        if (email.isEmpty()) {
            showStatus("Email is required.", false);
            return;
        }

        if (contactNumber.isEmpty()) {
            showStatus("Contact number is required.", false);
            return;
        }

        if (!password.isEmpty() && !password.equals(confirmPassword)) {
            showStatus("Passwords do not match.", false);
            return;
        }

        currentStudent.setEmail(email);
        currentStudent.setContactNumber(contactNumber);
        currentStudent.setProfilePicture(profilePicture);

        try {
            boolean success = studentService.updateStudentProfile(
                    currentStudent.getUserId(),
                    email,
                    contactNumber,
                    profilePicture,
                    password
            );

            if (success) {
                passwordField.clear();
                confirmPasswordField.clear();
                showStatus("Profile updated successfully.", true);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully.");
            } else {
                showStatus("Failed to update profile.", false);
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update profile.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Unexpected error occurred.", false);
            showAlert(Alert.AlertType.ERROR, "Error", "Unexpected error occurred.");
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StudentDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showStatus("Failed to load dashboard.", false);
        }
    }

    private void showStatus(String message, boolean success) {
        statusLabel.setText(message);
        statusLabel.setStyle(success
                ? "-fx-text-fill: #28a745; -fx-font-size: 12px; -fx-font-weight: bold;"
                : "-fx-text-fill: #ff6b6b; -fx-font-size: 12px; -fx-font-weight: bold;");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}