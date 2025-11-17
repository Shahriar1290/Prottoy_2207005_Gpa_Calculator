package com.example.prottoy_2207005_gpa_calculator.controllers;

import com.example.prottoy_2207005_gpa_calculator.models.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ResultController {

    @FXML private TableView<Course> resultTable;
    @FXML private TableColumn<Course, String> rName;
    @FXML private TableColumn<Course, String> rCode;
    @FXML private TableColumn<Course, Integer> rCredit;
    @FXML private TableColumn<Course, String> rT1;
    @FXML private TableColumn<Course, String> rT2;
    @FXML private TableColumn<Course, String> rGrade;

    @FXML private Label gpaLabel;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();

    public void setCourseList(ObservableList<Course> courses) {
        this.courseList = courses;
        populateTable();
        calculateGPA();
    }

    private void populateTable() {
        rName.setCellValueFactory(data -> data.getValue().nameProperty());
        rCode.setCellValueFactory(data -> data.getValue().codeProperty());
        rCredit.setCellValueFactory(data -> data.getValue().creditProperty().asObject());
        rT1.setCellValueFactory(data -> data.getValue().teacher1Property());
        rT2.setCellValueFactory(data -> data.getValue().teacher2Property());
        rGrade.setCellValueFactory(data -> data.getValue().gradeProperty());

        resultTable.setItems(courseList);
    }

    private void calculateGPA() {
        double totalPoints = 0;
        int totalCredits = 0;

        for (Course c : courseList) {
            double gradePoint = gradeToPoint(c.getGrade());
            totalPoints += gradePoint * c.getCredit();
            totalCredits += c.getCredit();
        }

        double gpa = totalCredits == 0 ? 0 : totalPoints / totalCredits;
        gpaLabel.setText(String.format("%.2f", gpa));
    }

    private double gradeToPoint(String grade) {
        return switch (grade) {
            case "A+" -> 4.0;
            case "A"  -> 3.75;
            case "A-" -> 3.50;
            case "B+" -> 3.25;
            case "B"  -> 3.00;
            case "B-" -> 2.75;
            case "C+" -> 2.50;
            case "C"  -> 2.25;
            case "D"  -> 2.00;
            case "F"  -> 0.0;
            default   -> 0.0;
        };
    }

    @FXML
    private void backHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/prottoy_2207005_gpa_calculator/Home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) gpaLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GPA Calculator Home");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
