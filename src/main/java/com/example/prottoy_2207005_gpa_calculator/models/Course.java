package com.example.prottoy_2207005_gpa_calculator.models;

import javafx.beans.property.*;

public class Course {

    private final StringProperty name;
    private final StringProperty code;
    private final IntegerProperty credit;
    private final StringProperty teacher1;
    private final StringProperty teacher2;
    private final StringProperty grade;

    public Course(String name, String code, int credit, String teacher1, String teacher2, String grade) {
        this.name = new SimpleStringProperty(name);
        this.code = new SimpleStringProperty(code);
        this.credit = new SimpleIntegerProperty(credit);
        this.teacher1 = new SimpleStringProperty(teacher1);
        this.teacher2 = new SimpleStringProperty(teacher2);
        this.grade = new SimpleStringProperty(grade);
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty codeProperty() { return code; }
    public IntegerProperty creditProperty() { return credit; }
    public StringProperty teacher1Property() { return teacher1; }
    public StringProperty teacher2Property() { return teacher2; }
    public StringProperty gradeProperty() { return grade; }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getCode() { return code.get(); }
    public void setCode(String code) { this.code.set(code); }

    public int getCredit() { return credit.get(); }
    public void setCredit(int credit) { this.credit.set(credit); }

    public String getTeacher1() { return teacher1.get(); }
    public void setTeacher1(String teacher1) { this.teacher1.set(teacher1); }

    public String getTeacher2() { return teacher2.get(); }
    public void setTeacher2(String teacher2) { this.teacher2.set(teacher2); }

    public String getGrade() { return grade.get(); }
    public void setGrade(String grade) { this.grade.set(grade); }
}
