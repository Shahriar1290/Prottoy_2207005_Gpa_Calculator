package com.example.prottoy_2207005_gpa_calculator.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private Button startButton;

    @FXML
    private void startGPA(ActionEvent event) {
        try {
            // Load entry scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/prottoy_2207005_gpa_calculator/Entry.fxml"));
            Parent root = loader.load();

            // Get current stage
            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GPA Entry");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
