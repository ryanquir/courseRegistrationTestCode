package edu.virginia.cs.hw4;

import org.junit.jupiter.api.*;

import java.time.DayOfWeek;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegistrationImplTest {
    private Course mockCourse;
    private Course mockCourse2;
    private Student mockStudent;
    private RegistrationImpl mockRegistration;
    private CourseCatalog mockCatalog;
    @BeforeEach
    public void setUpMock() {
        mockRegistration = new RegistrationImpl();
        mockCourse = mock(Course.class);
        mockCourse2 = mock(Course.class);
        mockStudent = mock(Student.class);
        Set<Course> courseList = new HashSet<>();
        courseList.add(mockCourse);
        courseList.add(mockCourse2);
        mockCatalog = new CourseCatalog(courseList);
        mockRegistration.setCourseCatalog(mockCatalog);
    }
    @Test
    public void testGetCatalog() {
        assertEquals(mockRegistration.getCourseCatalog(),mockRegistration.getCourseCatalog());
    }
    @Test
    public void testEnrollmentFull() {
        when(mockCourse.getCurrentEnrollmentSize()).thenReturn(5);
        when(mockCourse.getEnrollmentCap()).thenReturn(5);
        assertTrue(mockRegistration.isEnrollmentFull(mockCourse));
    }
    @Test
    public void testEnrollmentEmpty() {
        when(mockCourse.getCurrentEnrollmentSize()).thenReturn(4);
        when(mockCourse.getEnrollmentCap()).thenReturn(5);
        assertFalse(mockRegistration.isEnrollmentFull(mockCourse));
    }
    @Test
    public void testWaitListFull() {
        when(mockCourse.getCurrentWaitListSize()).thenReturn(5);
        when(mockCourse.getWaitListCap()).thenReturn(5);
        assertTrue(mockRegistration.isWaitListFull(mockCourse));
    }
    @Test
    public void testWaitListEmpty() {
        when(mockCourse.getCurrentWaitListSize()).thenReturn(4);
        when(mockCourse.getWaitListCap()).thenReturn(5);
        assertFalse(mockRegistration.isWaitListFull(mockCourse));
    }
    @Test
    public void testGetEnrollmentStatus() {
        assertEquals(mockCourse.getEnrollmentStatus(),mockRegistration.getEnrollmentStatus(mockCourse));
    }
    @Test
    public void testTimeConflict() {
        when(mockCourse.getMeetingStartTimeHour()).thenReturn(1);
        when(mockCourse.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse.getMeetingDays()).thenReturn(Collections.singletonList(DayOfWeek.MONDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(1);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(50);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse2.getMeetingDays()).thenReturn(Collections.singletonList(DayOfWeek.MONDAY));
        assertTrue(mockRegistration.areCoursesConflicted(mockCourse,mockCourse2));
    }
    @Test
    public void testNoTimeConflict() {
        when(mockCourse.getMeetingStartTimeHour()).thenReturn(1);
        when(mockCourse.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse.getMeetingDays()).thenReturn(Collections.singletonList(DayOfWeek.MONDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(2);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse2.getMeetingDays()).thenReturn(Collections.singletonList(DayOfWeek.MONDAY));
        assertFalse(mockRegistration.areCoursesConflicted(mockCourse,mockCourse2));
    }
    @Test
    public void testScheduleConflict() {
        when(mockCourse.isStudentEnrolled(mockStudent)).thenReturn(true);
        when(mockCourse.getMeetingStartTimeHour()).thenReturn(1);
        when(mockCourse.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse.getMeetingDays()).thenReturn(Collections.singletonList(DayOfWeek.MONDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(1);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(50);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse2.getMeetingDays()).thenReturn(Collections.singletonList(DayOfWeek.MONDAY));
        assertTrue(mockRegistration.hasConflictWithStudentSchedule(mockCourse2,mockStudent));
    }
    @Test
    public void testNoScheduleConflict() {
        when(mockCourse.isStudentEnrolled(mockStudent)).thenReturn(true);
        when(mockCourse.getMeetingStartTimeHour()).thenReturn(1);
        when(mockCourse.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse.getMeetingDays()).thenReturn(Collections.singletonList(DayOfWeek.MONDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(2);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse2.getMeetingDays()).thenReturn(Collections.singletonList(DayOfWeek.MONDAY));
        assertFalse(mockRegistration.hasConflictWithStudentSchedule(mockCourse2,mockStudent));
    }
    @Test
    public void testMeetsPrerequisites() {
        List<Prerequisite> prerequisites = new ArrayList<>();
        Prerequisite prerequisite = new Prerequisite(mockCourse, Grade.A);
        prerequisites.add(prerequisite);
        when(mockStudent.meetsPrerequisite(prerequisite)).thenReturn(true);
        assertTrue(mockRegistration.hasStudentMeetsPrerequisites(mockStudent, prerequisites));
    }
    @Test
    public void testNotMeetingPrerequisites() {
        List<Prerequisite> prerequisites = new ArrayList<>();
        Prerequisite prerequisite = new Prerequisite(mockCourse, Grade.F);
        prerequisites.add(prerequisite);
        when(mockStudent.meetsPrerequisite(prerequisite)).thenReturn(false);
        assertFalse(mockRegistration.hasStudentMeetsPrerequisites(mockStudent, prerequisites));
    }
    @Test
    public void testRegCourseClosed() {
        when(mockCourse.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.CLOSED);
        assertEquals(RegistrationResult.COURSE_CLOSED, mockRegistration.registerStudentForCourse(mockStudent, mockCourse));
        verify(mockCourse, never()).addStudentToEnrolled(mockStudent);
    }
    @Test
    public void testRegCourseFull() {
        when(mockCourse.getCurrentEnrollmentSize()).thenReturn(5);
        when(mockCourse.getEnrollmentCap()).thenReturn(5);
        assertEquals(RegistrationResult.COURSE_FULL, mockRegistration.registerStudentForCourse(mockStudent, mockCourse));
        verify(mockCourse, never()).addStudentToEnrolled(mockStudent);
    }
    @Test
    public void testRegScheduleConflict() {
        when(mockCourse2.getCurrentEnrollmentSize()).thenReturn(4);
        when(mockCourse2.getEnrollmentCap()).thenReturn(5);
        when(mockCourse.isStudentEnrolled(mockStudent)).thenReturn(true);
        when(mockCourse.getMeetingStartTimeHour()).thenReturn(1);
        when(mockCourse.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse.getMeetingDays()).thenReturn(Collections.singletonList(DayOfWeek.MONDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(1);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(50);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse2.getMeetingDays()).thenReturn(Collections.singletonList(DayOfWeek.MONDAY));
        assertEquals(RegistrationResult.SCHEDULE_CONFLICT, mockRegistration.registerStudentForCourse(mockStudent, mockCourse2));
        verify(mockCourse, never()).addStudentToEnrolled(mockStudent);
        when(mockCourse2.getCurrentEnrollmentSize()).thenReturn(5);
        when(mockCourse2.getEnrollmentCap()).thenReturn(5);
        when(mockCourse2.getWaitListCap()).thenReturn(5);
        when(mockCourse2.getCurrentWaitListSize()).thenReturn(4);
        assertEquals(RegistrationResult.SCHEDULE_CONFLICT, mockRegistration.registerStudentForCourse(mockStudent, mockCourse2));
        verify(mockCourse, never()).addStudentToEnrolled(mockStudent);
    }
    @Test
    public void testRegPrerequisiteNotMet() {
        when(mockCourse.getCurrentEnrollmentSize()).thenReturn(4);
        when(mockCourse.getEnrollmentCap()).thenReturn(5);
        List<Prerequisite> prerequisites = new ArrayList<>();
        Prerequisite prerequisite = new Prerequisite(mockCourse, Grade.F);
        prerequisites.add(prerequisite);
        when(mockCourse.getPrerequisites()).thenReturn(prerequisites);
        when(mockStudent.meetsPrerequisite(prerequisite)).thenReturn(false);
        assertEquals(RegistrationResult.PREREQUISITE_NOT_MET, mockRegistration.registerStudentForCourse(mockStudent, mockCourse));
        verify(mockCourse, never()).addStudentToEnrolled(mockStudent);
        when(mockCourse.getCurrentEnrollmentSize()).thenReturn(5);
        when(mockCourse.getEnrollmentCap()).thenReturn(5);
        when(mockCourse.getWaitListCap()).thenReturn(5);
        when(mockCourse.getCurrentWaitListSize()).thenReturn(4);
        assertEquals(RegistrationResult.PREREQUISITE_NOT_MET, mockRegistration.registerStudentForCourse(mockStudent, mockCourse));
        verify(mockCourse, never()).addStudentToEnrolled(mockStudent);
    }
    @Test
    public void testEnrollStudent() {
        when(mockCourse.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.OPEN);
        when(mockCourse.getCurrentEnrollmentSize()).thenReturn(0);
        when(mockCourse.getEnrollmentCap()).thenReturn(1);
        assertEquals(RegistrationResult.ENROLLED, mockRegistration.registerStudentForCourse(mockStudent, mockCourse));
        verify(mockCourse).addStudentToEnrolled(mockStudent);
        assertEquals(mockCourse.getEnrollmentStatus(), Course.EnrollmentStatus.WAIT_LIST);
    }
    @Test
    public void testWaitListStudent() {
        when(mockCourse.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.WAIT_LIST);
        when(mockCourse.getCurrentEnrollmentSize()).thenReturn(1);
        when(mockCourse.getEnrollmentCap()).thenReturn(1);
        when(mockCourse.getCurrentWaitListSize()).thenReturn(0);
        when(mockCourse.getWaitListCap()).thenReturn(1);
        assertEquals(RegistrationResult.WAIT_LISTED, mockRegistration.registerStudentForCourse(mockStudent, mockCourse));
        verify(mockCourse).addStudentToWaitList(mockStudent);
        assertEquals(mockCourse.getEnrollmentStatus(), Course.EnrollmentStatus.CLOSED);
    }
}
