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
    private Map<Course,Grade> mockMap;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        mockStudent = getTestStudent();
        testCourse = mock(Course.class);
        mockMap = mock(Map.class);
        testTranscript = new Transcript(mockMap);
        mockStudent.setTranscript(testTranscript);
    }
    private Student getTestStudent() {
        return new Student(100, "Christopher Joseph",
                "tbh7cm@virginia.edu");
    }

    @Test
    public void testAddCourseGradeToEmptyTranscript() {
        when(mockMap.isEmpty()).thenReturn(true);
        mockStudent.addCourseGrade(testCourse,Grade.A);
        verify(mockMap).put(testCourse, Grade.A);
    }
    @Test
    public void testaddCourseGradetoNonEmptyTranscript() {
        when(mockMap.isEmpty()).thenReturn(false);
        mockStudent.addCourseGrade(testCourse,Grade.A);
        verify(mockMap).put(testCourse, Grade.A);
    }
    @Test
    public void testStudentAlreadyTookCourse() {
        when(mockMap.containsKey(testCourse)).thenReturn(true);
        assertTrue(mockStudent.hasStudentTakenCourse(testCourse));
    }
    @Test
    public void testStudentNeverTookCourse() {
        when(mockMap.containsKey(testCourse)).thenReturn(false);
        assertFalse(mockStudent.hasStudentTakenCourse(testCourse));
    }
    @Test
    public void testCantGetCourseGrade() {
        when(mockMap.containsKey(testCourse)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () ->
                mockStudent.getCourseGrade(testCourse));
        verify(mockMap, never()).get(testCourse);
    }
    @Test
    public void testGetCourseGrade() {
        when(mockMap.containsKey(testCourse)).thenReturn(true);
        when(mockMap.get(testCourse)).thenReturn(Grade.A_PLUS);
        Grade actual = mockStudent.getCourseGrade(testCourse);
        Grade expected = Grade.A_PLUS;
        assertEquals(expected,actual);
    }
    @Test
    @DisplayName("Passed Pre-req class")
    public void testMeetsPreRequisites() {
        //took pre-req and passed it
        Prerequisite prerequisite = new Prerequisite(testCourse,Grade.C);
        when(mockMap.containsKey(testCourse)).thenReturn(true);
        when(mockMap.get(testCourse)).thenReturn(Grade.B_PLUS);
        Course testCourse1 = mock(Course.class);
        testCourse1.addPrerequisite(prerequisite);
        assertTrue(mockStudent.meetsPrerequisite(prerequisite));
    }
    @Test
    @DisplayName("Did not take Pre-Req")
    public void testDidNotTakePreRequisiteClass() {
        Prerequisite prerequisite = new Prerequisite(testCourse,Grade.C);
        when(mockMap.containsKey(testCourse)).thenReturn(false);
        Course testCourse1 = mock(Course.class);
        testCourse1.addPrerequisite(prerequisite);
        assertFalse(mockStudent.meetsPrerequisite(prerequisite));
    }
    @Test
    @DisplayName("Took Pre-Req but did not pass it.")
    public void testFailsPreRequisites() {
        Prerequisite prerequisite = new Prerequisite(testCourse,Grade.C);
        when(mockMap.containsKey(testCourse)).thenReturn(true);
        when(mockMap.get(testCourse)).thenReturn(Grade.D_PLUS);
        Course testCourse1 = mock(Course.class);
        testCourse1.addPrerequisite(prerequisite);
        assertFalse(mockStudent.meetsPrerequisite(prerequisite));
    }
    @Test
    public void testGetGPAwithNoClasses() {
        when(mockMap.isEmpty()).thenReturn(true);
        assertThrows(IllegalStateException.class, () ->
                mockStudent.getGPA());
    }
    @Test
    public void testGetGPA() {
        Set<Course> courseList = new HashSet<>();
        courseList.add(testCourse);
        when(testCourse.getCreditHours()).thenReturn(3);
        when(mockMap.isEmpty()).thenReturn(false);
        when(mockMap.keySet()).thenReturn(courseList);
        when(mockMap.get(testCourse)).thenReturn(Grade.A_PLUS);
        double actual = mockStudent.getGPA();
        assertEquals(4.0,actual);
    }
}
