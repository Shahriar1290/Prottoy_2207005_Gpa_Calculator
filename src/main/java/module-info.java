module com.example.prottoy_2207005_gpa_calculator {
        requires javafx.controls;
        requires javafx.fxml;
        requires java.sql;

        opens com.example.prottoy_2207005_gpa_calculator to javafx.fxml;
        opens com.example.prottoy_2207005_gpa_calculator.controllers to javafx.fxml;
        opens com.example.prottoy_2207005_gpa_calculator.models to javafx.base;

        exports com.example.prottoy_2207005_gpa_calculator;
        exports com.example.prottoy_2207005_gpa_calculator.controllers;
        exports com.example.prottoy_2207005_gpa_calculator.models;
        }
