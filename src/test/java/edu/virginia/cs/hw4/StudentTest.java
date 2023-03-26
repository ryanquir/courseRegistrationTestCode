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
    private Map testHistory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        testCourse = mock(Course.class);
        testTranscript = mock(Transcript.class);
        mockStudent = getTestStudent();
        testTranscript.courseHistory = mock(Map.class);
//        mockEnrollment = (List<Student>) mock(List.class);
//        mockWaitList = (List<Student>) mock(List.class);
//        testCourse.setEnrolledStudents(mockEnrollment);
//        testCourse.setWaitListedStudents(mockWaitList);
    }
    private Student getTestStudent() {
        return new Student(100, "Christopher Joseph",
                "tbh7cm@virginia.edu", testTranscript);
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
        mockStudent.addCourseGrade(testCourse,Grade.A);
        verify(testTranscript).courseHistory.put(testCourse,Grade.A);
    }
    @Test
    public void testStudentAlreadyTookCourse() {
        when(testTranscript.courseHistory.containsKey(testCourse)).thenReturn(true);
        assertTrue(mockStudent.hasStudentTakenCourse(testCourse));
    }
    @Test
    public void testStudentNeverTookCourse() {
        assertFalse(mockStudent.hasStudentTakenCourse(testCourse));
    }
    @Test
    public void testCantGetCourseGrade() {
        when(testTranscript.courseHistory.containsKey(testCourse)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () ->
                mockStudent.getCourseGrade(testCourse));
        verify(testTranscript.courseHistory, never()).get(testCourse);
    }
    @Test
    public void testGetCourseGrade() {
        when(testTranscript.courseHistory.containsKey(testCourse)).thenReturn(true);
        verify(testTranscript.courseHistory).get(testCourse);
    }
    @Test
    @DisplayName("Passed Pre-req class")
    public void testMeetsPreRequisites() {
        //took pre-req and passed it

    }
    @Test
    @DisplayName("Did not take Pre-Req")
    public void testFailsPreRequisites1() {
        //did not take pre-req

    }
    @Test
    @DisplayName("Took Pre-Req but did not pass it.")
    public void testFailsPreRequisites2() {
        //took pre-req and failed it

    }
    @Test
    public void testGetGPAwithNoClasses() {
        when(testTranscript.courseHistory.isEmpty()).thenReturn(true);
        assertThrows(IllegalStateException.class, () ->
                mockStudent.getGPA());
        verify(testTranscript.courseHistory, never()).get(testCourse);
    }
    @Test
    public void testGetGPA() {
        mockStudent.addCourseGrade(testCourse,Grade.A_PLUS);
        assertEquals(4.0,mockStudent.getGPA());
    }
}
