package com.example.prottoy_2207005_gpa_calculator.controllers;

import com.example.prottoy_2207005_gpa_calculator.models.Course;
import com.sun.javafx.menu.MenuItemBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EntryController {

    @FXML private Label totalCreditLabel;
    @FXML private Label currentCreditLabel;

    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField courseCreditField;
    @FXML private TextField teacher1Field;
    @FXML private TextField teacher2Field;
    @FXML private ComboBox<String> gradeCombo;

    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> colName;
    @FXML private TableColumn<Course, String> colCode;
    @FXML private TableColumn<Course, Integer> colCredit;
    @FXML private TableColumn<Course, String> colT1;
    @FXML private TableColumn<Course, String> colT2;
    @FXML private TableColumn<Course, String> colGrade;

    @FXML private Button calculateButton;
    @FXML private Button addCourseButton;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private int totalCredits = 18; // Example, you can make it dynamic
    private int currentCredits = 0;

    @FXML
    public void initialize() {
        gradeCombo.setItems(FXCollections.observableArrayList(
                "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "D", "F"
        ));

        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colCode.setCellValueFactory(data -> data.getValue().codeProperty());
        colCredit.setCellValueFactory(data -> data.getValue().creditProperty().asObject());
        colT1.setCellValueFactory(data -> data.getValue().teacher1Property());
        colT2.setCellValueFactory(data -> data.getValue().teacher2Property());
        colGrade.setCellValueFactory(data -> data.getValue().gradeProperty());

        courseTable.setItems(courseList);

        totalCreditLabel.setText(String.valueOf(totalCredits));
        currentCreditLabel.setText(String.valueOf(currentCredits));

        calculateButton.setDisable(true);
    }

    @FXML
    private void addCourse() {
        try {
            String name = courseNameField.getText().trim();
            String code = courseCodeField.getText().trim();
            int credit = Integer.parseInt(courseCreditField.getText().trim());
            String t1 = teacher1Field.getText().trim();
            String t2 = teacher2Field.getText().trim();
            String grade = gradeCombo.getValue();

            if (name.isEmpty() || code.isEmpty() || t1.isEmpty() || t2.isEmpty() || grade == null) {
                showAlert("Please fill all fields!");
                return;
            }

            if (currentCredits + credit > totalCredits) {
                showAlert("Adding this course exceeds total allowed credits!");
                return;
            }

            Course c = new Course(name, code, credit, t1, t2, grade);
            courseList.add(c);

            currentCredits += credit;
            currentCreditLabel.setText(String.valueOf(currentCredits));

            courseNameField.clear();
            courseCodeField.clear();
            courseCreditField.clear();
            teacher1Field.clear();
            teacher2Field.clear();
            gradeCombo.setValue(null);

            calculateButton.setDisable(currentCredits != totalCredits);

            addCourseButton.setDisable(currentCredits >= totalCredits);


        } catch (NumberFormatException e) {
            showAlert("Invalid credit value!");
        }
    }



    @FXML
    private void calculateGPA() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/prottoy_2207005_gpa_calculator/Result.fxml"));
            Parent root = loader.load();

            ResultController controller = loader.getController();
            controller.setCourseList(courseList);

            Stage stage = (Stage) calculateButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GPA Result");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
