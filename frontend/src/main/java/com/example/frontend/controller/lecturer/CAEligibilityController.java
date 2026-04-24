package com.example.frontend.controller.lecturer;

import com.example.frontend.model.CAEligibilityRow;
import com.example.frontend.model.LecturerCourseItem;
import com.example.frontend.network.ServerClient;
import com.example.frontend.service.CAEligibilityService;
import com.example.frontend.service.LecturerCourseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.util.List;

public class CAEligibilityController {

    @FXML private ComboBox<LecturerCourseItem> courseBox;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;
    @FXML private Label summaryLabel;

    @FXML private TableView<CAEligibilityRow> caTable;
    @FXML private TableColumn<CAEligibilityRow, String> regNoColumn;
    @FXML private TableColumn<CAEligibilityRow, String> nameColumn;
    @FXML private TableColumn<CAEligibilityRow, String> courseColumn;
    @FXML private TableColumn<CAEligibilityRow, String> caMarksColumn;
    @FXML private TableColumn<CAEligibilityRow, String> caMaxColumn;
    @FXML private TableColumn<CAEligibilityRow, String> caPercentageColumn;
    @FXML private TableColumn<CAEligibilityRow, String> statusColumn;

    private LecturerCourseService lecturerCourseService;
    private CAEligibilityService caEligibilityService;

    private final ObservableList<CAEligibilityRow> masterList =
            FXCollections.observableArrayList();

    private FilteredList<CAEligibilityRow> filteredList;

    @FXML
    public void initialize() {
        lecturerCourseService = new LecturerCourseService(ServerClient.getInstance());
        caEligibilityService = new CAEligibilityService(ServerClient.getInstance());

        setupCourseBox();
        setupTable();
        loadCourses();
        hideStatus();
    }

    private void setupCourseBox() {
        courseBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(LecturerCourseItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getCourseName() + " | " + item.getCourseId());
            }
        });

        courseBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(LecturerCourseItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Select Course" : item.getCourseName() + " | " + item.getCourseId());
            }
        });
    }

    private void setupTable() {
        regNoColumn.setCellValueFactory(data ->
                new SimpleStringProperty(safe(data.getValue().getRegNo())));

        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(safe(data.getValue().getStudentName())));

        courseColumn.setCellValueFactory(data ->
                new SimpleStringProperty(safe(data.getValue().getCourseCode()) + " - " + safe(data.getValue().getCourseName())));

        caMarksColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCaMarks())));

        caMaxColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCaMaxMarks())));

        caPercentageColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCaPercentage() + "%"));

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(safe(data.getValue().getEligibilityStatus())));

        caTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(CAEligibilityRow item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setStyle("");
                } else if ("Eligible".equalsIgnoreCase(item.getEligibilityStatus())) {
                    setStyle("-fx-background-color: #f0fff4;");
                } else {
                    setStyle("-fx-background-color: #fff5f5;");
                }
            }
        });

        filteredList = new FilteredList<>(masterList, p -> true);
        caTable.setItems(filteredList);
    }

    private void loadCourses() {
        try {
            List<LecturerCourseItem> courses = lecturerCourseService.getLecturerCourses();

            if (courses == null || courses.isEmpty()) {
                showError("No assigned courses found.");
                return;
            }

            courseBox.setItems(FXCollections.observableArrayList(courses));
            courseBox.getSelectionModel().selectFirst();

            loadCAEligibility();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load courses.");
        }
    }

    @FXML
    private void loadCAEligibility() {
        LecturerCourseItem selectedCourse = courseBox.getValue();

        if (selectedCourse == null) {
            showError("Please select course.");
            return;
        }

        List<CAEligibilityRow> rows =
                caEligibilityService.getCAEligibility(selectedCourse.getCourseId());

        masterList.setAll(rows);
        filterTable();

        long eligible = rows.stream()
                .filter(r -> "Eligible".equalsIgnoreCase(r.getEligibilityStatus()))
                .count();

        summaryLabel.setText(
                rows.size() + " students | " +
                eligible + " eligible | " +
                (rows.size() - eligible) + " not eligible"
        );

        if (rows.isEmpty()) {
            showInfo("No CA eligibility records found.");
        } else {
            showSuccess("CA eligibility loaded.");
        }
    }

    @FXML
    private void filterTable() {
        String keyword = searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase();

        if (filteredList == null) return;

        filteredList.setPredicate(row -> {
            if (keyword.isEmpty()) return true;

            return safe(row.getRegNo()).toLowerCase().contains(keyword)
                    || safe(row.getStudentName()).toLowerCase().contains(keyword)
                    || safe(row.getCourseName()).toLowerCase().contains(keyword)
                    || safe(row.getCourseCode()).toLowerCase().contains(keyword)
                    || safe(row.getEligibilityStatus()).toLowerCase().contains(keyword);
        });
    }

    @FXML
    private void clearSearch() {
        searchField.clear();
        filterTable();
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/lecturer/lecturerDashboard.fxml")
            );

            Parent root = loader.load();
            statusLabel.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Unable to go back.");
        }
    }

    private String safe(String text) {
        return text == null ? "" : text;
    }

    private void showSuccess(String message) {
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
    }

    private void showError(String message) {
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
    }

    private void showInfo(String message) {
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #8fa3b8; -fx-font-weight: bold;");
    }

    private void hideStatus() {
        statusLabel.setText("");
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
    }
}