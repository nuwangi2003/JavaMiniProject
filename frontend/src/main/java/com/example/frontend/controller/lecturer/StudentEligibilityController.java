package com.example.frontend.controller.lecturer;

import com.example.frontend.model.LecturerCourseItem;
import com.example.frontend.model.StudentEligibilityRow;
import com.example.frontend.network.ServerClient;
import com.example.frontend.service.LecturerCourseService;
import com.example.frontend.service.StudentEligibilityService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.util.List;

public class StudentEligibilityController {

    @FXML private ComboBox<LecturerCourseItem> courseBox;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;
    @FXML private Label summaryLabel;

    @FXML private TableView<StudentEligibilityRow> eligibilityTable;
    @FXML private TableColumn<StudentEligibilityRow, String> regNoColumn;
    @FXML private TableColumn<StudentEligibilityRow, String> nameColumn;
    @FXML private TableColumn<StudentEligibilityRow, String> courseColumn;
    @FXML private TableColumn<StudentEligibilityRow, String> totalHoursColumn;
    @FXML private TableColumn<StudentEligibilityRow, String> attendedHoursColumn;
    @FXML private TableColumn<StudentEligibilityRow, String> percentageColumn;
    @FXML private TableColumn<StudentEligibilityRow, String> statusColumn;

    private LecturerCourseService lecturerCourseService;
    private StudentEligibilityService eligibilityService;

    private final ObservableList<StudentEligibilityRow> masterList =
            FXCollections.observableArrayList();

    private FilteredList<StudentEligibilityRow> filteredList;

    @FXML
    public void initialize() {
        lecturerCourseService = new LecturerCourseService(ServerClient.getInstance());
        eligibilityService = new StudentEligibilityService(ServerClient.getInstance());

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
                new SimpleStringProperty(
                        safe(data.getValue().getCourseCode()) + " - " + safe(data.getValue().getCourseName())
                ));

        totalHoursColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getTotalHours())));

        attendedHoursColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getAttendedHours())));

        percentageColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAttendancePercentage() + "%"));

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(safe(data.getValue().getEligibilityStatus())));

        eligibilityTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(StudentEligibilityRow item, boolean empty) {
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
        eligibilityTable.setItems(filteredList);
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

            loadEligibility();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load assigned courses.");
        }
    }

    @FXML
    private void loadEligibility() {
        LecturerCourseItem selectedCourse = courseBox.getValue();

        if (selectedCourse == null) {
            showError("Please select a course.");
            return;
        }

        List<StudentEligibilityRow> rows =
                eligibilityService.getEligibilityByCourse(selectedCourse.getCourseId());

        masterList.setAll(rows);
        filterTable();

        long eligibleCount = rows.stream()
                .filter(r -> "Eligible".equalsIgnoreCase(r.getEligibilityStatus()))
                .count();

        summaryLabel.setText(
                rows.size() + " students | " +
                eligibleCount + " eligible | " +
                (rows.size() - eligibleCount) + " not eligible"
        );

        if (rows.isEmpty()) {
            showInfo("No eligibility records found.");
        } else {
            showSuccess("Eligibility loaded successfully.");
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