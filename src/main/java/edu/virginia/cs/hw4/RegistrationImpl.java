package edu.virginia.cs.hw4;

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
        return false;
    }

    @Override
    public boolean hasConflictWithStudentSchedule(Course course, Student student) {
        return false;
    }

    @Override
    public boolean hasStudentMeetsPrerequisites(Student student, List<Prerequisite> prerequisites) {
        return false;
    }

    @Override
    public RegistrationResult registerStudentForCourse(Student student, Course course) {
        return null;
    }

    @Override
    public void dropCourse(Student student, Course course) {

    }
}
