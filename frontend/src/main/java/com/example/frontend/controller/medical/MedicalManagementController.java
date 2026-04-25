package com.example.frontend.controller.medical;

import com.example.frontend.controller.admin.LoginController;
import com.example.frontend.model.Medical;
import com.example.frontend.service.MedicalService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MedicalManagementController {
    private static final double AUTO_REFRESH_SECONDS = 5.0;


    @FXML
    private TextField batchField;
    @FXML
    private ComboBox<String> statusFilterCombo;
    @FXML
    private TableView<Medical> recordsTable;
    @FXML
    private TableColumn<Medical, Integer> idColumn;
    @FXML
    private TableColumn<Medical, String> studentColumn;
    @FXML
    private TableColumn<Medical, String> courseColumn;
    @FXML
    private TableColumn<Medical, String> examTypeColumn;
    @FXML
    private TableColumn<Medical, String> dateColumn;
    @FXML
    private TableColumn<Medical, String> copyColumn;
    @FXML
    private TableColumn<Medical, String> statusColumn;
    @FXML
    private TextField medicalIdField;
    @FXML
    private Label statusLabel;

    private final MedicalService medicalService = new MedicalService(LoginController.client);
    private final ObservableList<Medical> visibleRecords = FXCollections.observableArrayList();
    private final Timeline autoRefreshTimeline = new Timeline(
            new KeyFrame(javafx.util.Duration.seconds(AUTO_REFRESH_SECONDS), event -> refreshDisplayedRecords(false))
    );

    @FXML
    public void initialize() {
        statusFilterCombo.getItems().addAll("All", "Pending", "Approved", "Rejected");
        statusFilterCombo.getSelectionModel().select("All");
        configureTable();
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
        showStatus("", StatusType.INFO);
    }

    @FXML
    private void loadBatchRecords() {
        refreshDisplayedRecords(true);
    }

    private void refreshDisplayedRecords(boolean showFeedback) {
        String batch = batchField.getText() == null ? "" : batchField.getText().trim();

        if (batch.isEmpty()) {
            visibleRecords.clear();
            recordsTable.getSelectionModel().clearSelection();
            medicalIdField.clear();
            if (showFeedback) {
                showStatus("Enter batch first.", StatusType.ERROR);
            }
            return;
        }

        List<Medical> records = medicalService.getBatchMedicalRecords(batch);
        String statusFilter = statusFilterCombo.getValue();
        List<Medical> filteredRecords = filterRecords(records, statusFilter);
        Integer selectedMedicalId = getSelectedMedicalId();

        visibleRecords.setAll(filteredRecords);
        recordsTable.setItems(visibleRecords);

        boolean hasVisibleRecords = !filteredRecords.isEmpty();
        restoreSelection(selectedMedicalId);

        if (showFeedback) {
            showStatus(hasVisibleRecords ? "Records loaded successfully." : medicalService.getLastMessage(),
                    hasVisibleRecords ? StatusType.SUCCESS : StatusType.ERROR);
        }
    }

    @FXML
    private void approveSelected() {
        Integer id = readMedicalId();
        if (id == null) {
            return;
        }

        Medical record = findVisibleRecord(id);
        if (record != null && "Approved".equalsIgnoreCase(record.getStatus())) {
            showStatus("Medical #" + id + " is already approved.", StatusType.INFO);
            return;
        }

        boolean ok = medicalService.approveMedical(id);
        showStatus(ok ? "Medical #" + id + " approved." : medicalService.getLastMessage(),
                ok ? StatusType.SUCCESS : StatusType.ERROR);

        if (ok) {
            if (record != null) {
                record.setStatus("Approved");
                recordsTable.refresh();
            }
            refreshDisplayedRecords(false);
        }
    }

    @FXML
    private void rejectSelected() {
        Integer id = readMedicalId();
        if (id == null) {
            return;
        }

        Medical record = findVisibleRecord(id);
        if (record != null && "Rejected".equalsIgnoreCase(record.getStatus())) {
            showStatus("Medical #" + id + " is already rejected.", StatusType.INFO);
            return;
        }

        boolean ok = medicalService.rejectMedical(id);
        showStatus(ok ? "Medical #" + id + " rejected." : medicalService.getLastMessage(),
                ok ? StatusType.SUCCESS : StatusType.ERROR);

        if (ok) {
            if (record != null) {
                record.setStatus("Rejected");
                recordsTable.refresh();
            }
            refreshDisplayedRecords(false);
        }
    }

    @FXML
    private void backToDashboard() {
        loadView("/view/techofficer/techOfficerDashboard.fxml");
    }

    private Integer readMedicalId() {
        try {
            String typedValue = medicalIdField.getText();
            if (typedValue != null && !typedValue.isBlank()) {
                return Integer.parseInt(typedValue.trim());
            }
        } catch (Exception e) {
            showStatus("Enter a valid medical id.", StatusType.ERROR);
            return null;
        }

        Medical selectedRecord = recordsTable.getSelectionModel().getSelectedItem();
        if (selectedRecord != null && selectedRecord.getMedicalId() != null) {
            medicalIdField.setText(String.valueOf(selectedRecord.getMedicalId()));
            return selectedRecord.getMedicalId();
        }

        showStatus("Select a medical record or enter a valid medical id.", StatusType.ERROR);
        return null;
    }

    private List<Medical> filterRecords(List<Medical> records, String statusFilter) {
        if (records == null || records.isEmpty()) {
            return List.of();
        }

        List<Medical> filtered = new ArrayList<>();
        for (Medical m : records) {
            if (!"All".equalsIgnoreCase(statusFilter) && !statusFilter.equalsIgnoreCase(m.getStatus())) {
                continue;
            }
            filtered.add(m);
        }

        return filtered;
    }

    private Integer getSelectedMedicalId() {
        Medical selectedRecord = recordsTable.getSelectionModel().getSelectedItem();
        if (selectedRecord != null && selectedRecord.getMedicalId() != null) {
            return selectedRecord.getMedicalId();
        }

        try {
            String text = medicalIdField.getText();
            return text == null || text.isBlank() ? null : Integer.parseInt(text.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private Medical findVisibleRecord(Integer medicalId) {
        if (medicalId == null) {
            return null;
        }

        for (Medical record : visibleRecords) {
            if (medicalId.equals(record.getMedicalId())) {
                return record;
            }
        }
        return null;
    }

    private void restoreSelection(Integer selectedMedicalId) {
        recordsTable.getSelectionModel().clearSelection();
        medicalIdField.clear();

        if (visibleRecords.isEmpty()) {
            return;
        }

        if (selectedMedicalId != null) {
            for (Medical record : visibleRecords) {
                if (selectedMedicalId.equals(record.getMedicalId())) {
                    recordsTable.getSelectionModel().select(record);
                    return;
                }
            }
        }

        recordsTable.getSelectionModel().selectFirst();
    }

    private void configureTable() {
        recordsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMedicalId()));
        studentColumn.setCellValueFactory(data -> new SimpleStringProperty(value(data.getValue().getStudentId())));
        courseColumn.setCellValueFactory(data -> new SimpleStringProperty(value(data.getValue().getCourseId())));
        examTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(value(data.getValue().getExamType())));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(value(data.getValue().getDateSubmitted())));
        copyColumn.setCellValueFactory(data -> new SimpleStringProperty(value(data.getValue().getMedicalCopy())));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(value(data.getValue().getStatus())));

        recordsTable.setItems(visibleRecords);
        recordsTable.setPlaceholder(new Label("No medical records loaded."));
        recordsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selected) -> {
            if (selected != null && selected.getMedicalId() != null) {
                medicalIdField.setText(String.valueOf(selected.getMedicalId()));
            }
        });
    }

    private String value(String v) {
        return v == null ? "-" : v;
    }

    private void loadView(String path) {
        try {
            autoRefreshTimeline.stop();
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
        statusLabel.setText(message);

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
