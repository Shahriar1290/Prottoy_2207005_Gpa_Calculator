package com.example.prottoy_2207005_gpa_calculator.controllers;

import com.example.prottoy_2207005_gpa_calculator.models.Course;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class DatabaseTableController {

    @FXML private TableView<Course> courseTable;

    @FXML private TableColumn<Course, String> colCode;
    @FXML private TableColumn<Course, String> colName;
    @FXML private TableColumn<Course, Integer> colCredit;
    @FXML private TableColumn<Course, String> colT1;
    @FXML private TableColumn<Course, String> colT2;
    @FXML private TableColumn<Course, String> colGrade;

    @FXML
    public void initialize() {
        DatabaseHelper.createTable();

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        colT1.setCellValueFactory(new PropertyValueFactory<>("teacher1"));
        colT2.setCellValueFactory(new PropertyValueFactory<>("teacher2"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        refreshTable();
    }

    public void setCourseList(ObservableList<Course> list) {
        if (list == null) return;
        for (Course c : list) {
            DatabaseHelper.insertCourse(c);
        }
        refreshTable();
    }

    public void refreshTable() {
        ObservableList<Course> list = DatabaseHelper.fetchAllCourses();
        courseTable.setItems(list);
    }

    @FXML
    public void addCourse() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter Course details:\ncode,name,credit,teacher1,teacher2,grade");
        dialog.setContentText("Input:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) return;

        try {
            String[] parts = result.get().split(",");
            if (parts.length != 6) {
                showAlert("Invalid input format! You must enter 6 values: code,name,credit,teacher1,teacher2,grade");
                return;
            }
            String code = parts[0].trim();
            String name = parts[1].trim();
            int credit = Integer.parseInt(parts[2].trim());
            String t1 = parts[3].trim();
            String t2 = parts[4].trim();
            String grade = parts[5].trim();

            Course c = new Course(code, name, credit, t1, t2, grade);
            DatabaseHelper.insertCourse(c);
            refreshTable();
        } catch (Exception e) {
            showAlert("Error adding course: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void updateCourse() {
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a course to update.");
            return;
        }

        String initial = selected.getName() + "," + selected.getCredit() + "," + selected.getTeacher1() + "," + selected.getTeacher2() + "," + selected.getGrade();
        TextInputDialog dialog = new TextInputDialog(initial);
        dialog.setHeaderText("Update fields:\nname,credit,teacher1,teacher2,grade");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) return;

        try {
            String[] parts = result.get().split(",");
            if (parts.length != 5) {
                showAlert("Invalid input format! Expected 5 values.");
                return;
            }
            selected.setName(parts[0].trim());
            selected.setCredit(Integer.parseInt(parts[1].trim()));
            selected.setTeacher1(parts[2].trim());
            selected.setTeacher2(parts[3].trim());
            selected.setGrade(parts[4].trim());

            DatabaseHelper.updateCourse(selected);
            refreshTable();
        } catch (Exception e) {
            showAlert("Error updating course: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private double convertGradeToPoint(String grade) {
        switch (grade.toUpperCase()) {
            case "A+": return 4.0;
            case "A":  return 3.75;
            case "A-": return 3.50;
            case "B+": return 3.25;
            case "B":  return 3.0;
            case "B-": return 2.75;
            case "C+": return 2.50;
            case "C":  return 2.25;
            case "D":  return 2.0;
            case "F":  return 0.0;
            default:   return 0.0;
        }
    }

    @FXML
    public void calculateGPA() {
        ObservableList<Course> courses = courseTable.getItems();
        if (courses == null || courses.isEmpty()) {
            showAlert("No courses available to calculate GPA.");
            return;
        }

        double totalQualityPoints = 0;
        int totalCredits = 0;

        for (Course c : courses) {
            double gp = convertGradeToPoint(c.getGrade());
            totalQualityPoints += gp * c.getCredit();
            totalCredits += c.getCredit();
        }

        if (totalCredits == 0) {
            showAlert("Total credits cannot be zero!");
            return;
        }

        double gpa = totalQualityPoints / totalCredits;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GPA Result");
        alert.setHeaderText("Your Calculated GPA");
        alert.setContentText(String.format("GPA: %.2f", gpa));
        alert.showAndWait();
    }

    @FXML
    public void deleteCourse() {
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a course to delete.");
            return;
        }
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Delete course " + selected.getCode() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> res = a.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.YES) {
            DatabaseHelper.deleteCourse(selected.getCode());
            refreshTable();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
