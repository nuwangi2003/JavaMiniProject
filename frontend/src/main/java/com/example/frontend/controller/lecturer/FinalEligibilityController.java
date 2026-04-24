package com.example.frontend.controller.lecturer;

import com.example.frontend.model.FinalEligibilityRow;
import com.example.frontend.model.LecturerCourseItem;
import com.example.frontend.network.ServerClient;
import com.example.frontend.service.FinalEligibilityService;
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

public class FinalEligibilityController {

    @FXML private ComboBox<LecturerCourseItem> courseBox;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;
    @FXML private Label summaryLabel;

    @FXML private TableView<FinalEligibilityRow> finalEligibilityTable;

    @FXML private TableColumn<FinalEligibilityRow, String> regNoColumn;
    @FXML private TableColumn<FinalEligibilityRow, String> nameColumn;
    @FXML private TableColumn<FinalEligibilityRow, String> courseColumn;

    @FXML private TableColumn<FinalEligibilityRow, String> totalHoursColumn;
    @FXML private TableColumn<FinalEligibilityRow, String> attendedHoursColumn;
    @FXML private TableColumn<FinalEligibilityRow, String> medicalHoursColumn;
    @FXML private TableColumn<FinalEligibilityRow, String> finalAttendanceHoursColumn;
    @FXML private TableColumn<FinalEligibilityRow, String> attendancePercentageColumn;
    @FXML private TableColumn<FinalEligibilityRow, String> attendanceStatusColumn;

    @FXML private TableColumn<FinalEligibilityRow, String> caMarksColumn;
    @FXML private TableColumn<FinalEligibilityRow, String> caMaxColumn;
    @FXML private TableColumn<FinalEligibilityRow, String> caPercentageColumn;
    @FXML private TableColumn<FinalEligibilityRow, String> caStatusColumn;

    @FXML private TableColumn<FinalEligibilityRow, String> finalStatusColumn;

    private LecturerCourseService lecturerCourseService;
    private FinalEligibilityService finalEligibilityService;

    private final ObservableList<FinalEligibilityRow> masterList =
            FXCollections.observableArrayList();

    private FilteredList<FinalEligibilityRow> filteredList;

    @FXML
    public void initialize() {
        lecturerCourseService = new LecturerCourseService(ServerClient.getInstance());
        finalEligibilityService = new FinalEligibilityService(ServerClient.getInstance());

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

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCourseName() + " | " + item.getCourseId());
                }
            }
        });

        courseBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(LecturerCourseItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText("Select Course");
                } else {
                    setText(item.getCourseName() + " | " + item.getCourseId());
                }
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
                new SimpleStringProperty(format(data.getValue().getTotalHours())));

        attendedHoursColumn.setCellValueFactory(data ->
                new SimpleStringProperty(format(data.getValue().getAttendedHours())));

        medicalHoursColumn.setCellValueFactory(data ->
                new SimpleStringProperty(format(data.getValue().getMedicalHours())));

        finalAttendanceHoursColumn.setCellValueFactory(data ->
                new SimpleStringProperty(format(data.getValue().getFinalAttendanceHours())));

        attendancePercentageColumn.setCellValueFactory(data ->
                new SimpleStringProperty(format(data.getValue().getAttendancePercentage()) + "%"));

        attendanceStatusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(safe(data.getValue().getAttendanceStatus())));

        caMarksColumn.setCellValueFactory(data ->
                new SimpleStringProperty(format(data.getValue().getCaMarks())));

        caMaxColumn.setCellValueFactory(data ->
                new SimpleStringProperty(format(data.getValue().getCaMaxMarks())));

        caPercentageColumn.setCellValueFactory(data ->
                new SimpleStringProperty(format(data.getValue().getCaPercentage()) + "%"));

        caStatusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(safe(data.getValue().getCaStatus())));

        finalStatusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(safe(data.getValue().getFinalEligibilityStatus())));

        finalEligibilityTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(FinalEligibilityRow item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setStyle("");
                } else if ("Eligible".equalsIgnoreCase(item.getFinalEligibilityStatus())) {
                    setStyle("-fx-background-color: #f0fff4;");
                } else {
                    setStyle("-fx-background-color: #fff5f5;");
                }
            }
        });

        filteredList = new FilteredList<>(masterList, p -> true);
        finalEligibilityTable.setItems(filteredList);
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

            loadFinalEligibility();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load courses.");
        }
    }

    @FXML
    private void loadFinalEligibility() {
        LecturerCourseItem selectedCourse = courseBox.getValue();

        if (selectedCourse == null) {
            showError("Please select course.");
            return;
        }

        List<FinalEligibilityRow> rows =
                finalEligibilityService.getFinalEligibility(selectedCourse.getCourseId());

        masterList.setAll(rows);
        filterTable();

        long eligible = rows.stream()
                .filter(r -> "Eligible".equalsIgnoreCase(r.getFinalEligibilityStatus()))
                .count();

        long notEligible = rows.size() - eligible;

        summaryLabel.setText(
                rows.size() + " students | " +
                        eligible + " eligible | " +
                        notEligible + " not eligible"
        );

        if (rows.isEmpty()) {
            showInfo("No final eligibility records found.");
        } else {
            showSuccess("Final eligibility loaded successfully.");
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
                    || safe(row.getAttendanceStatus()).toLowerCase().contains(keyword)
                    || safe(row.getCaStatus()).toLowerCase().contains(keyword)
                    || safe(row.getFinalEligibilityStatus()).toLowerCase().contains(keyword);
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

    private String format(double value) {
        return String.format("%.2f", value);
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