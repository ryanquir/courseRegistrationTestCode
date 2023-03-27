package edu.virginia.cs.hw4;

import java.time.DayOfWeek;
import java.util.List;

public class RegistrationImpl implements Registration {
    //TODO: Implement class
    CourseCatalog myCatalog = new CourseCatalog();
    @Override
    public CourseCatalog getCourseCatalog() {
        return (CourseCatalog) myCatalog.getAllCourses();
    }

    @Override
    public void setCourseCatalog(CourseCatalog courseCatalog) {
        myCatalog.courseList = courseCatalog.courseList;
    }

    @Override
    public boolean isEnrollmentFull(Course course) {
        if (course.getCurrentEnrollmentSize() >= course.getEnrollmentCap()){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isWaitListFull(Course course) {
        if (course.getCurrentWaitListSize() >= course.getWaitListCap()){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Course.EnrollmentStatus getEnrollmentStatus(Course course) {
        return course.getEnrollmentStatus();
    }

    @Override
    public boolean areCoursesConflicted(Course first, Course second) {
        List<DayOfWeek> firstDays = first.getMeetingDays();
        List<DayOfWeek> secondDays = second.getMeetingDays();
        for (DayOfWeek day1:firstDays) {
            for (DayOfWeek day2:secondDays) {
                if (day1 == day2) {
                    int minutes1 = (first.getMeetingStartTimeHour()*60) + first.getMeetingStartTimeMinute();
                    int minutes2 = (second.getMeetingStartTimeHour()*60) + second.getMeetingStartTimeMinute();
                    for (int i = minutes1; i <= (minutes1+first.getMeetingDurationMinutes()); i++) {
                        for (int j = minutes2; j <= (minutes2+second.getMeetingDurationMinutes()); j++) {
                            if (i==j) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasConflictWithStudentSchedule(Course course, Student student) {
        List <Course> newCatalog = (List<Course>) getCourseCatalog();
        for (Course course1:newCatalog) {
            if(course1.isStudentEnrolled(student)) {
                if (areCoursesConflicted(course, course1)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasStudentMeetsPrerequisites(Student student, List<Prerequisite> prerequisites) {
        for (Prerequisite prerequisite:prerequisites) {
            if(!student.meetsPrerequisite(prerequisite)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public RegistrationResult registerStudentForCourse(Student student, Course course) {
        if (course.getEnrollmentStatus() == Course.EnrollmentStatus.CLOSED) {
            return RegistrationResult.COURSE_CLOSED;
        }
        if (isEnrollmentFull(course)) {
            if (isWaitListFull(course)) {
                return RegistrationResult.COURSE_FULL;
            }
            if (hasConflictWithStudentSchedule(course, student)) {
                return RegistrationResult.SCHEDULE_CONFLICT;
            }
            if (!hasStudentMeetsPrerequisites(student, course.getPrerequisites())){
                return RegistrationResult.PREREQUISITE_NOT_MET;
            }
            course.addStudentToWaitList(student);
            if (isWaitListFull(course)) {
                course.setEnrollmentStatus(Course.EnrollmentStatus.CLOSED);
            }
            return RegistrationResult.WAIT_LISTED;
        }
        if (hasConflictWithStudentSchedule(course, student)) {
            return RegistrationResult.SCHEDULE_CONFLICT;
        }
        if (!hasStudentMeetsPrerequisites(student, course.getPrerequisites())){
            return RegistrationResult.PREREQUISITE_NOT_MET;
        }
        course.addStudentToEnrolled(student);
        if (isEnrollmentFull(course)) {
            course.setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
        }
        return RegistrationResult.ENROLLED;
    }

    @Override
    public void dropCourse(Student student, Course course) {
        if (course.isStudentWaitListed(student)) {
            course.removeStudentFromWaitList(student);
            if (course.getEnrollmentStatus() == Course.EnrollmentStatus.CLOSED) {
                course.setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
            }
        } else if (course.isStudentEnrolled(student)) {
            course.removeStudentFromEnrolled(student);
            if (course.getEnrollmentStatus() == Course.EnrollmentStatus.WAIT_LIST) {
                if (course.getWaitListedStudents().isEmpty()) {
                    course.setEnrollmentStatus(Course.EnrollmentStatus.OPEN);
                } else {
                    Student x = course.getFirstStudentOnWaitList();
                    course.removeStudentFromWaitList(x);
                    course.addStudentToEnrolled(x);
                }
            }
        } else {
            throw new RuntimeException("IllegalArgumentException");
        }
    }
}
