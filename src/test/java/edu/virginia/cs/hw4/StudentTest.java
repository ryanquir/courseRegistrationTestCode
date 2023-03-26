package edu.virginia.cs.hw4;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class StudentTest {
    private Course testCourse;
    private Student mockStudent;
    private Transcript testTranscript;

    @BeforeEach
    public void setUp() {
        testCourse = mock(Course.class);
        mockStudent = getTestStudent();
        testTranscript = new Transcript(mockStudent);
    }
    private Student getTestStudent() {
        return new Student(100, "Christopher Joseph",
                "tbh7cm@virginia.edu");
    }
    private Course getTestCourse() {
        return new Course(18802,
                "Software Development Essentials", "CS",
                3140, 1, 3, "Paul McBurney",
                "pm8fc@virginia.edu", 175, 99,
                List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY),
                12, 30, 75,"Gilmer 301",
                new ArrayList<>());
    }
    //To Check:
    //void addCourseGrade(Course course, Grade grade)
    //boolean hasStudentTakenCourse(Course course)
    //Grade getCourseGrade(Course course)
    //boolean meetsPrerequisite(Prerequisite prerequisite)
    //double getGPA()
    @Test
    public void testaddCourseGrade() {
        //need to test for side effects
        mockStudent.addCourseGrade(testCourse,Grade.A);
        testTranscript = mockStudent.getTranscript();
        //assert that the student's transcript added the test course.
        assertTrue(testTranscript.courseHistory.containsKey(testCourse));
        //assert that the student's class on transcript has the correct grade.
        assertEquals(Grade.A,testTranscript.courseHistory.get(testCourse));
    }
    @Test
    public void testStudentAlreadyTookCourse() {
        mockStudent.addCourseGrade(testCourse,Grade.A);
        assertTrue(mockStudent.hasStudentTakenCourse(testCourse));
    }
    @Test
    public void testStudentNeverTookCourse() {
        Course testCourse1 = mock(Course.class);
        mockStudent.addCourseGrade(testCourse,Grade.A);
        assertFalse(mockStudent.hasStudentTakenCourse(testCourse1));
    }
    @Test
    public void testCantGetCourseGrade() {
        //we did not add a class this time, so student should not have any classes to get a grade from.
        assertThrows(IllegalArgumentException.class, () ->
                mockStudent.getCourseGrade(testCourse));
    }
    @Test
    public void testGetCourseGrade() {
        mockStudent.addCourseGrade(testCourse,Grade.A);
        Grade actual = mockStudent.getCourseGrade(testCourse);
        Grade expected = Grade.A;
        assertEquals(expected,actual);
    }
    @Test
    @DisplayName("Passed Pre-req class")
    public void testMeetsPreRequisites() {
        //took pre-req and passed it
        Prerequisite prerequisite = new Prerequisite(testCourse,Grade.C);
        mockStudent.addCourseGrade(testCourse,Grade.B_PLUS);
        Course testCourse1 = mock(Course.class);
        testCourse1.addPrerequisite(prerequisite);
        assertTrue(mockStudent.meetsPrerequisite(prerequisite));
    }
    @Test
    @DisplayName("Did not take Pre-Req")
    public void testDidNotTakePreRequisiteClass() {
        //did not take pre-req
        Prerequisite prerequisite = new Prerequisite(testCourse,Grade.C);
        Course testCourse1 = mock(Course.class);
        testCourse1.addPrerequisite(prerequisite);
        assertFalse(mockStudent.meetsPrerequisite(prerequisite));
    }
    @Test
    @DisplayName("Took Pre-Req but did not pass it.")
    public void testFailsPreRequisites() {
        //took pre-req and failed it
        Prerequisite prerequisite = new Prerequisite(testCourse,Grade.C);
        mockStudent.addCourseGrade(testCourse,Grade.D_PLUS);
        Course testCourse1 = mock(Course.class);
        testCourse1.addPrerequisite(prerequisite);
        assertFalse(mockStudent.meetsPrerequisite(prerequisite));
    }
    @Test
    public void testGetGPAwithNoClasses() {
        assertThrows(IllegalStateException.class, () ->
                mockStudent.getGPA());
    }
    @Test
    public void testGetGPA() {
        when(testCourse.getCreditHours()).thenReturn(3);
        mockStudent.addCourseGrade(testCourse,Grade.A_PLUS);
        double actual = mockStudent.getGPA();
        assertEquals(4.0,actual);
    }
}
