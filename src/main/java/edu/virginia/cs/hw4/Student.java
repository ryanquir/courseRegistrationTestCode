package edu.virginia.cs.hw4;

public class Student {
    private int studentNumber;
    private String name;
    private String email;

    private Transcript transcript;

    public Student(int studentNumber, String name, String email) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.email = email;
        this.transcript = new Transcript(this);
    }

    public Student(int studentNumber, String name, String email, Transcript transcript) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.email = email;
        this.transcript = transcript;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addCourseGrade(Course course, Grade grade) {
        transcript.courseHistory.put(course, grade);
    }

    public boolean hasStudentTakenCourse(Course course) {
        return transcript.courseHistory.containsKey(course);
    }

    public Grade getCourseGrade(Course course) {
        if (hasStudentTakenCourse(course)) {
            transcript.courseHistory.get(course);
        }
        throw new IllegalArgumentException("ERROR: Student has no grade for " + course);
    }

    public boolean meetsPrerequisite(Prerequisite prerequisite) {
        if (!hasStudentTakenCourse(prerequisite.course)) {
            return false;
        }
        Grade studentGrade = transcript.courseHistory.get(prerequisite.course);
        return studentGrade.gpa >= prerequisite.minimumGrade.gpa;
    }

    public double getGPA() {
        if (transcript.courseHistory.isEmpty()) {
            throw new IllegalStateException("No courses taken, cannot get GPA");
        }
        double totalGradePoints = 0.0;
        int creditsAttempted = 0;
        for (Course course : transcript.courseHistory.keySet()) {
            Grade grade = transcript.courseHistory.get(course);
            int credits = course.getCreditHours();
            totalGradePoints += grade.gpa * credits;
            creditsAttempted += credits;
        }
        return totalGradePoints / creditsAttempted;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Student otherStudent) {
            return this.studentNumber == otherStudent.studentNumber;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return studentNumber;
    }
}
