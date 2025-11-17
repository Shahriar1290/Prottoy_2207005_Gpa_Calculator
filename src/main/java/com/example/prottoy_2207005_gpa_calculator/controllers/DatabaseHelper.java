package com.example.prottoy_2207005_gpa_calculator.controllers;

import com.example.prottoy_2207005_gpa_calculator.models.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DatabaseHelper {

    private static final String URL = "jdbc:sqlite:gpa.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }


    public static void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS course (
                    code TEXT PRIMARY KEY,
                    name TEXT,
                    credit INTEGER,
                    teacher1 TEXT,
                    teacher2 TEXT,
                    grade TEXT
                );
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void insertCourse(Course course) {
        String sql = "INSERT INTO course(code, name, credit, teacher1, teacher2, grade) VALUES(?,?,?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCode());
            pstmt.setString(2, course.getName());
            pstmt.setInt(3, course.getCredit());
            pstmt.setString(4, course.getTeacher1());
            pstmt.setString(5, course.getTeacher2());
            pstmt.setString(6, course.getGrade());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void updateCourse(Course course) {
        String sql = """
                UPDATE course SET name=?, credit=?, teacher1=?, teacher2=?, grade=?
                WHERE code=?
                """;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getName());
            pstmt.setInt(2, course.getCredit());
            pstmt.setString(3, course.getTeacher1());
            pstmt.setString(4, course.getTeacher2());
            pstmt.setString(5, course.getGrade());
            pstmt.setString(6, course.getCode());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteCourse(Course course) {
        String sql = "DELETE FROM course WHERE code=?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCode());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static ObservableList<Course> fetchAllCourses() {
        ObservableList<Course> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM course";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Course c = new Course(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getInt("credit"),
                        rs.getString("teacher1"),
                        rs.getString("teacher2"),
                        rs.getString("grade")
                );
                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
