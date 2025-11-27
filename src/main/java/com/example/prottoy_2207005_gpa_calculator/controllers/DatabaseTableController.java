package com.example.prottoy_2207005_gpa_calculator.controllers;

import com.example.prottoy_2207005_gpa_calculator.models.Course;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class DatabaseTableController {

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

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

    private void refreshTable() {
        ObservableList<Course> courses = DatabaseHelper.fetchAllCourses();
        courseTable.setItems(courses);
    }


    @FXML
    public void addCourse(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter Course details:\ncode,name,credit,teacher1,teacher2,grade");
        dialog.setContentText("Input:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) return;

        try {
            String[] parts = result.get().split(",");
            if (parts.length != 6) {
                showAlert("Invalid input format! You must enter 6 values.");
                return;
            }

            String code = parts[0].trim();
            String name = parts[1].trim();
            int credit = Integer.parseInt(parts[2].trim());
            String t1 = parts[3].trim();
            String t2 = parts[4].trim();
            String grade = parts[5].trim();

            Course c = new Course(code, name, credit, t1, t2, grade);
            CourseCRUD.insertCourse(c);

            refreshTable();

        } catch (Exception e) {
            showAlert("Error adding course!");
            e.printStackTrace();
        }
    }


    @FXML
    public void updateCourse(ActionEvent event) {

        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a course to update.");
            return;
        }

        String initialText = selected.getName() + "," +
                selected.getCredit() + "," +
                selected.getTeacher1() + "," +
                selected.getTeacher2() + "," +
                selected.getGrade();

        TextInputDialog dialog = new TextInputDialog(initialText);
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

            CourseCRUD.updateCourse(selected);
            refreshTable();

        } catch (Exception e) {
            showAlert("Error updating course!");
            e.printStackTrace();
        }
    }


    @FXML
    public void deleteClicked(ActionEvent event) {

        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a course to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete course " + selected.getCode() + "?",
                ButtonType.YES, ButtonType.NO);

        if (confirm.showAndWait().get() == ButtonType.YES) {
            try {
                CourseCRUD.deleteCourse(selected);
                refreshTable();
            } catch (Exception e) {
                showAlert("Error deleting course!");
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
