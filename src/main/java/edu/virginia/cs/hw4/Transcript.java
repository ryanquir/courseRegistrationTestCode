package edu.virginia.cs.hw4;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Transcript {
    Student student;
    Map<Course, Grade> courseHistory;

    //void addCourseGrade(Course course, Grade grade)
    //boolean hasStudentTakenCourse(Course course)
    //Grade getCourseGrade(Course course)
    //boolean meetsPrerequisite(Prerequisite prerequisite)
    //double getGPA()
    public Transcript(Student student) {
        this.student = student;
        courseHistory = new HashMap<>();
    }
    public void addCourseGrade(Course course, Grade grade) {
        courseHistory.put(course, grade);
    }
    public boolean checkForCourse(Course course) {
        return courseHistory.containsKey(course);
    }
    public Grade getCourseGrade(Course course) {
        return courseHistory.get(course);
    }
    public boolean isEmpty() {
        return courseHistory.isEmpty();
    }
    public Set<Course> setofCourses() {
        return courseHistory.keySet();
    }

}
