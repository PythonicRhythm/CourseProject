package CourseProject;

import java.util.ArrayList;

/**
 * This class simulates a course that a student can take at a university. It contains
 * informations like the course name, the credit hours for the course and there is a plan
 * for a future implementation for who is teaching the course and for what degree path the
 * class is required for.
 */

public class Course {
    
    // TODO: Implement major-locked courses using a majors instance variable. 
    private String courseName;
    private int creditHours;
    private String letterGrade;
    private ArrayList<Course> prereqs = new ArrayList<Course>();

    public Course(String name, int hours) {
        this.courseName = name;
        this.creditHours = hours;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setGrade(String grade) {
        letterGrade = grade;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void addPreReq(Course c) {
        prereqs.add(c);
    }


}
