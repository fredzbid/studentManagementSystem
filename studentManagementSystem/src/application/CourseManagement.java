package application;

import java.util.ArrayList;
import java.util.List;

public class CourseManagement {
    private static List<Course> courses = new ArrayList<>();

    public static void addCourse(String courseCode, String name, double courseWeight, int maxCapacity) {
        Course course = new Course(courseCode, name, courseWeight, maxCapacity);
        courses.add(course);
    }

    public static void enrollStudent(Student student, Course course) {
        course.enrollStudent(student);
    }

    public static List<Course> getCourses() {
        return courses;
    }

    // Method to assign grade to a student for a course
    public static void assignGrade(Student student, Course course, double grade) {
        if (course.getEnrolledStudents().contains(student)) {
        	student.setGrade(course.getName(), grade);
        	} else {
            System.out.println("Student is not enrolled in this course."); // Testing
        }
    }
}