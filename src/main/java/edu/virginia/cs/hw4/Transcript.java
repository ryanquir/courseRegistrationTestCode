package edu.virginia.cs.hw4;

import java.util.HashMap;
import java.util.Map;

public class Transcript {
    Student student;
    Map<Course, Grade> courseHistory;

    public Transcript(Student student) {
        this.student = student;
        courseHistory = new HashMap<>();
    }
}
