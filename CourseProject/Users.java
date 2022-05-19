package CourseProject;


import java.util.ArrayList;

/**
 * This class simulates a student's information while attending a university. It has their
 * account information like their name, username, and password and also contains a list of
 * their registered courses for their university. Future implementation plans are the presence
 * of Superusers to interact with different parts of the course system.
 */

public class Users {

    // TODO: Implement majors variable that works with the courses class.
    // TODO: Implement a clean superuser that can make changes to Student instances.
    private final String firstName;
    private final String lastName;
    private String username;
    private String password;
    private final String idNumber;
    private ArrayList<Course> coursesTaken = new ArrayList<Course>();
    private ArrayList<Course> registeredCourses = new ArrayList<Course>();

    public Users(String user, String pass, String first, String last, String id) {
        this.username = user;
        this.password = pass;
        this.firstName = first;
        this.lastName = last;
        this.idNumber = id;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getID() {
        return idNumber;
    }

    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }

    public int getTotalRegisteredHours() {
        int total = 0;
        for(Course c: registeredCourses) {
            total = total + c.getCreditHours();
        }
        return total;
    }

    public int getTotalTakenHours() {
        int total = 0;
        for(Course c: coursesTaken) {
            total = total + c.getCreditHours();
        }
        return total;
    }

    public ArrayList<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    public ArrayList<Course> getTakenCourses() {
        return coursesTaken;
    }

    public void setUsername(String newUsername) {
        username = newUsername;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }

    public void addTakenCourses(Course c) {
        coursesTaken.add(c);
    }

    public void removeTakenCourses(Course c) {
        coursesTaken.remove(c);
    }

    public void addRegisteredCourse(Course c) {
        registeredCourses.add(c);
    }

    public void removeRegisteredCourse(Course c) {
        registeredCourses.remove(c);
    }

    public double getGPA() {
        double totalPoints = 0;
        double totalHoursAttempted = 0;
        for(Course c: coursesTaken) {
            switch(c.getLetterGrade()) {
                case "A+":
                    totalPoints += 4.3 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                case "A":
                    totalPoints += 4.0 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                case "A-":
                    totalPoints += 3.7 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                case "B+":
                    totalPoints += 3.3 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                case "B":
                    totalPoints += 3.0 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                case "B-":
                    totalPoints += 2.7 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                case "C+":
                    totalPoints += 2.3 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                case "C":
                    totalPoints += 2.0 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                case "C-":
                    totalPoints += 1.7 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                case "D+":
                    totalPoints += 1.3 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                case "D":
                    totalPoints += 1.0 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                case "D-":
                    totalPoints += 0.7 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                case "F":
                    totalPoints += 0.0 * c.getCreditHours();
                    totalHoursAttempted += c.getCreditHours();
                    break;
                default:
                    System.out.println("MAJOR ERROR: STUDENT TAKEN COURSE WITH NO GRADE");
            }
        }
        return totalPoints / totalHoursAttempted;
    }
    
    public String getHoursStatus() {
        if(getTotalRegisteredHours() >= 12) {
            return "Full-Time Student";
        }
        else {
            return "Part-Time Student";
        }
    }
}
