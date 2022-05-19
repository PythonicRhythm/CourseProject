package CourseProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This program simulates a university's student course management software. It contains
 * a Users class that contains a students information from name to the courses they have 
 * registered in the university. There is a Course class that simulates a course in the
 * university that the students can register for.
 */

 
// TODO: URGENTLY requires a saving function... no information changes are currently able to be saved.

public class Main {

    private static ArrayList<Course> courses = new ArrayList<>();
    private static ArrayList<Users> students = new ArrayList<>();
    private static Users activeUser;
    private static Scanner input = new Scanner(System.in);

    // Will collect all the courses that are made available at the university via txt file.
    private static void initialCourseSetup() throws FileNotFoundException {
        Scanner courseFile = new Scanner(new File("CourseProject\\CourseList.txt"));
        
        while (courseFile.hasNextLine()) {
            String line = courseFile.nextLine();
            String[] info = line.split("/");
            Course newCourse = new Course(info[0], Integer.parseInt(info[1]));
            courses.add(newCourse);
        }
        //for(Course c: courses) {
         //   System.out.println(c.getCourseName());
        //}
        courseFile.close();


    }

    // Will collect all the students currently registered at the university and all of their
    // personal information including all the courses they registered for or have taken in the past.
    // This method will use all the courses collected and put them into an arraylist while assigning them
    // to an instance of a User Class (student) and finally organize all of the students into a list.
    private static void initialUsersSetup() throws FileNotFoundException {
        Scanner usersFile = new Scanner(new File("CourseProject\\StudentsList.txt"));

        while (usersFile.hasNextLine()) {
            String line = usersFile.nextLine();
            String[] info = line.split("/");
            Users existingUser = new Users(info[2], info[3], info[0], info[1], info[4]);
            ArrayList<String> takenCourses = collectTakenCourses(info[4]);
            for(String takenCourse: takenCourses) {
                String[] nameAndGrade = takenCourse.split(":");
                for(Course course: courses) {
                    if(nameAndGrade[0].equals(course.getCourseName())) {
                        Course copiedCourse = new Course(course.getCourseName(), course.getCreditHours());
                        copiedCourse.setGrade(nameAndGrade[1]);
                        existingUser.addTakenCourses(copiedCourse);
                    }
                }
            }
            for(String regCourse: collectRegisteredCourses(info[4])) {
                for(Course course: courses) {
                    if(regCourse.equals(course.getCourseName())) {
                        Course copiedCourse = new Course(course.getCourseName(), course.getCreditHours());
                        existingUser.addRegisteredCourse(copiedCourse);
                    }
                }
            }
            students.add(existingUser);
        }
        usersFile.close();
        
    }

    // Will return an ArrayList of the Course names that a specific user has already taken in the university 
    // to the initialUsersSetup method.
    private static ArrayList<String> collectTakenCourses(String specificID) throws FileNotFoundException {
        Scanner userCourseFile = new Scanner(new File("CourseProject\\StudentsCourses.txt"));
        ArrayList<String> takenCourses = new ArrayList<String>();
        
        while(userCourseFile.hasNextLine()) {
            String id = userCourseFile.next().strip();
            if(id.equals(specificID)) {
                String line = userCourseFile.nextLine().strip();
                String[] takenClasses = line.split("/");
                for(String c: takenClasses) {
                    takenCourses.add(c);
                }
                userCourseFile.close();
                return takenCourses;
            }
            else {
                userCourseFile.nextLine();
                userCourseFile.nextLine();
            }
        }

        System.out.println("MAJOR ERROR: COLLECT TAKEN COURSES RETURNING NULL");
        userCourseFile.close();
        return null;
    }

    // Will return an ArrayList of the Course names that a specific user has registered for the current or upcoming semester.
    private static ArrayList<String> collectRegisteredCourses(String specificID) throws FileNotFoundException {
        Scanner userCourseFile = new Scanner(new File("CourseProject\\StudentsCourses.txt"));
        ArrayList<String> registeredCourses = new ArrayList<String>();
        
        while(userCourseFile.hasNextLine()) {
            String id = userCourseFile.next().strip();
            if(id.equals(specificID)) {
                userCourseFile.nextLine();
                String line = userCourseFile.nextLine().strip();
                String[] registeredClasses = line.split("/");
                for(String c: registeredClasses) {
                    registeredCourses.add(c);
                }
                userCourseFile.close();
                return registeredCourses;
            }
            else {
                userCourseFile.nextLine();
                userCourseFile.nextLine();
            }
        }

        System.out.println("MAJOR ERROR: COLLECT TAKEN COURSES RETURNING NULL");
        userCourseFile.close();
        return null;
    }

    // This method will clear the console for aesthetic and security reasons.
    private static void clearConsoleHelper() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ex) {}
    }
    
    // This is the main loop of the program.
    public static void main(String[] args) throws FileNotFoundException {
        
        initialCourseSetup();
        initialUsersSetup();

        while(true) {
            loginMenu();
            mainMenu();
        }
        
    }
    
    // This method will recieve the user's login information and if it matches an existing account, the program will retrieve
    // all of the user's saved information and continue on to the main menu. This method also has a attempt counter where if the user
    // exceeds 5 attempts then the program will close and they will be locked out. (Future experimentation on this will occur for fun.)
    private static void loginMenu() {
        System.out.println("""

                ||        ||||||||  ||     ||
                ||        ||        ||     ||
                ||        ||||||||  ||     ||
                ||              ||  ||     ||
                ||||||||  ||||||||  |||||||||
                """);
        System.out.println("""
            Welcome to Louisiana State University!
            Please sign in so we can service you!
            Type 'exit' at anytime to quit.
            """);
        int attempts = 0;
        while(true) {
            if(attempts > 5) {
                System.out.println("Too Many Attempts. Program will close.");
                input.close();
                System.exit(0);
            }
            System.out.print("Username: ");
            String user = input.nextLine().toLowerCase().strip();
            if(user.toLowerCase().equals("exit")) {System.exit(0);}
            System.out.print("Password: ");
            String pass = input.nextLine().strip();
            if(pass.toLowerCase().equals("exit")) {System.exit(0);}

            for (Users student: students) {
                if(user.equals(student.getUsername()) && pass.equals(student.getPassword())) {
                    clearConsoleHelper();
                    activeUser = student;
                    return;
                }
            }
            System.out.println("Login failed. Please try again.");
            attempts++;
        }
    }

    // This is the main menu method. This method will display all the choices the now logged in user can make.
    // The user can enter the Course Booklet, Course Schedule, and Student Profile menu from the main menu.
    // There will be a greater amount of options for the user in the future.
    private static void mainMenu() {
        System.out.printf("""

                What would you like to access, %s?

                |  Course Booklet  |  Course Schedule |
                |------------------|------------------|
                |  Student Profile |  College Record  |

                Type 'cb' for Course Booklet, 'cs' for Course Schedule,
                'sp' for Student Profile, 'cr' for College Record, or 
                'exit' to end the program. You can also type 'logout' 
                and return to the login menu.

                """, activeUser.getFirstName());
        
        while(true) {
            System.out.print("> ");
            String response = input.nextLine().toLowerCase().strip();

            if(response.equals("exit")) {
                input.close();
                System.exit(0);
            }
            else if(response.equals("cb")) {
                cbMenu();
                break;
            }
            else if(response.equals("cs")) {
                csMenu();
                break;
            }
            else if(response.equals("sp")) {
                spMenu();
                break;
            }
            else if(response.equals("cr")) {
                crMenu();
                break;
            }
            else if(response.equals("logout")) {
                clearConsoleHelper();
                return;
            }
            else {System.out.println("\nInvalid Input. Try Again.\n");}
        }
    }

    // This method is the Course Booklet menu. This method will display all the courses avaliable at LSU.
    // This menu is very basic for now but in the future all the courses will be organized and presented in
    // a cleaner, and more efficient fashion for our "students".
    // TODO: Implement different booklets for different semesters like fall or spring.
    private static void cbMenu() {
        clearConsoleHelper();
        System.out.print("""

                Welcome to the LSU Course Booklet!

                All Courses in all Semesters:

                Class Name | Credit Hours |

                """);
        for(Course c: courses) {
            if(c.getCourseName().length() == 9) {
                System.out.println(c.getCourseName() + "         " + c.getCreditHours());
            }
            else {
                System.out.println(c.getCourseName() + "          " + c.getCreditHours());
            }
        }

        System.out.print("""

            Take your time and view our options!
            When you're ready to return to the main
            menu, type 'menu' or type 'exit' to close
            the program.

            """);
        while(true) {
            System.out.print("> ");
            String response = input.nextLine().toLowerCase().strip();
            if(response.equals("exit")) {
                input.close();
                System.exit(0);
            }
            else if(response.equals("menu")) {
                clearConsoleHelper();
                mainMenu();
                break;
            }
            else {
                System.out.println("\nInvalid input. Try Again.\n");
            }
        }
    }

    // This method is the Course Schedule menu. This menu displays all the courses the student has registered for. This method
    // also allows the student to add or remove courses. There is a restriction on credit hours allowed for students (currently 17)
    // and if the student attempts to exceed this limit, they will not be allowed to add the course.
    private static void csMenu() {
        clearConsoleHelper();
        System.out.printf("""
            
            This is your Course Schedule %s.

            Class Name | Credit Hours |

            """, activeUser.getFirstName());

        for(Course c: activeUser.getRegisteredCourses()) {
            System.out.println(c.getCourseName() + "         " + c.getCreditHours());
        }

        System.out.printf("""

            Total Credit Hours = %d Hours
            Max Credit Hours = 17 Hours
            """, activeUser.getTotalRegisteredHours());

        System.out.print("""
            
            To add a course, type 'add'.
            To remove a course, type 'remove'.
            To return to the main menu, type 'menu'.
            To exit the program, type 'exit'.

            """);
        
        while(true) {
            System.out.print("> ");
            String response = input.nextLine().toLowerCase().strip();

            if(response.equals("exit")) {
                input.close();
                System.exit(0);
            }
            else if(response.equals("add")) {
                if(activeUser.getTotalRegisteredHours() >= 17) {
                    System.out.println("\nYou cannot have more hours than 17 unless specifically allowed.\nContact a counselor for further help.\n");
                }
                else{
                    addCourse();
                    csMenu();
                    break;
                }
            }
            else if(response.equals("remove")) {
                removeCourse();
                csMenu();
                break;
            }
            else if(response.equals("menu")) {
                clearConsoleHelper();
                mainMenu();
                break;
            }
            else {System.out.println("\nInvalid Input. Try Again.\n");}

        }
    }

    // This method is the Student Profile Menu. This menu displays all of the User's information required by the university.
    // Currently just full name, School ID, username, and password. This method also allows the changing of student's usernames and passwords.
    private static void spMenu() {
        clearConsoleHelper();
        System.out.printf("%nThis is your Student Profile, %s.%n%n", activeUser.getFirstName());

        System.out.printf("Full Name: %s%n", activeUser.getFullName());
        System.out.printf("ID Number: %s%n", activeUser.getID());
        System.out.printf("Username: %s%n", activeUser.getUsername());
        System.out.printf("Password: %s%n", activeUser.getPassword());
        System.out.printf("Hours Status: %s%n", activeUser.getHoursStatus());

        System.out.print("""

            If you would like to change your username or password,
            type 'change'. If you would like to return to the main
            menu, type 'menu'. You can also type 'exit' to end the program.
            
            """);

        while(true) {
            System.out.print("> ");
            String response = input.nextLine().toLowerCase().strip();
            if(response.equals("exit")) {
                input.close();
                System.exit(0);
            }
            else if(response.equals("menu")) {
                clearConsoleHelper();
                mainMenu();
                break;
            }
            else if(response.equals("change")) {
                changeUserInformation();
                spMenu();
                break;
            }
            else {
                System.out.print("\nInvalid Input. Try Again.\n\n");
            }
        }
        
    }

    // This method is the College Record menu. This menu displays the User's complete record of all the classes he/she has taken
    // and the User's GPA. You can return to the menu or exit the program from this menu. Plan to somehow print this screen.
    private static void crMenu() {
        clearConsoleHelper();
        System.out.printf("""

        LOUISIANA STATE UNIVERSITY
        COLLEGE RECORD:

        %s %s

        COURSE\t\tGRADE\tHOURS

        """, activeUser.getFullName(), activeUser.getID());

        for(Course c: activeUser.getTakenCourses()) {
            System.out.println(c.getCourseName() + "\t" + c.getLetterGrade() + "\t" + c.getCreditHours());
        }

        System.out.printf("""

        Total Record Credit Hours: %d
        Total Record GPA: %.2f

        Type 'menu' to return to the main menu. Type 'print' to
        print this page. Type 'exit' to exit the program.

        """, activeUser.getTotalTakenHours() , activeUser.getGPA());

        while(true) {
            System.out.print("> ");
            String response = input.nextLine().toLowerCase().strip();
            if(response.equals("exit")) {
                input.close();
                System.exit(0);
            }
            else if(response.equals("menu")) {
                clearConsoleHelper();
                mainMenu();
                break;
            }
            else if(response.equals("print")) {
                System.out.println("Not implemented yet loser.");
            }
            else {
                System.out.println("Invalid Input. Try Again.");
            }
        }

    }

    // This will confirm that the user is inputting something for the course name.
    // Confirming the coursename is not an empty string.
    private static String csMenuInputHelper() {
        System.out.print("\nEnter Course Name.\n");
        while(true) {
            String courseName = "";
            System.out.print("> ");
            courseName = input.nextLine().toLowerCase().strip();
            if(courseName.equals("")) {
                continue;
            }
            else {
                return courseName;
            }
        }
    }

    // This method that works from the Student Profile menu allows the user to change some of their information.
    // Currently it allows only the ability to change their Username and Password but there will be more information
    // requested from the university such as a School email and home address.
    // This also has some input validation for the username and password like length requirements and usernames cant be the same as others.
    private static void changeUserInformation() {
        System.out.print("""

            Which would you like to change?
            'user' for username. 'pass' for password.
            'back' to go back to the Profile Menu.
            
            """);
        
        while(true) {
            System.out.print("> ");
            String response = input.nextLine().toLowerCase().strip();

            if(response.equals("user")) {
                while(true) {
                    System.out.print("\nType 'back' to go back.\nNew Username: ");
                    String newUser = input.nextLine().toLowerCase().strip();
                    if(newUser.equals("back")) {
                        return;
                    }
                    else if(usernameTakenHelper(newUser)) {
                        System.out.println("\nThis username is already taken. Try another.");
                    }
                    else if(newUser.length() > 10) {
                        System.out.println("\nThis username is too long. Cannot be greater than 10 characters.");
                    }
                    else if(newUser.length() < 5) {
                        System.out.println("\nThis username is too short. Cannot be less than 5 characters.");
                    }
                    else {
                        System.out.printf("""

                            Are you sure about '%s' being your new Username?
                            Type 'y' for yes and 'n' for no.

                            """, newUser);
                        while(true) {
                            System.out.print("> ");
                            String confirmation = input.nextLine().toLowerCase().strip();
                            if(confirmation.equals("y")) {
                                activeUser.setUsername(newUser);
                                return;
                            }
                            else if(confirmation.equals("n")) {
                                return;
                            }
                            else {
                                System.out.print("\nInvalid Input. Try Again.\n");
                            }
                        }
                    }
                }
            }

            else if(response.equals("pass")) {
                while(true) {
                    System.out.print("\nType 'back' to go back.\nNew Password: ");
                    String newPass = input.nextLine().strip();
                    if(newPass.equals("back")) {
                        return;
                    }
                    else if(newPass.length() > 10) {
                        System.out.println("\nThis password is too long. Cannot be greater than 10 characters.");
                    }
                    else if(newPass.length() < 5) {
                        System.out.println("\nThis password is too short. Cannot be less than 5 characters.");
                    }
                    else {
                        System.out.printf("""

                            Are you sure about '%s' being your new Password?
                            Type 'y' for yes and 'n' for no.

                            """, newPass);
                        while(true) {
                            System.out.print("> ");
                            String confirmation = input.nextLine().toLowerCase();
                            if(confirmation.equals("y")) {
                                activeUser.setPassword(newPass);
                                return;
                            }
                            else if(confirmation.equals("n")) {
                                return;
                            }
                            else {
                                System.out.print("\nInvalid Input. Try Again.\n");
                            }
                        }
                    }
                }
            }

            else if(response.equals("back")) {
                break;
            }

            else {
                System.out.println("Invalid Input. Try Again.");
            }
        }
    }

    // This method in the Course Schedule menu allows the user to add a course to their Schedule.
    // This method confirms that the course exists in the university and that the user hasnt already registered
    // for it in this semester. Future implementation will allow for checking if there are too many credit hours 
    // in one semester and also if the Student has taken the course in previous semesters.
    // TODO: Implement a restriction on what courses students are allowed to take. (Major locked classes.)
    private static void addCourse() {
        while(true) {
            boolean validCourse = false;
            Course toBeAdded = null;
            String courseName = csMenuInputHelper();

            if(courseName.equals("exit")) {
                break;
            }
            
            for(Course c: courses) {
                if(c.getCourseName().toLowerCase().equals(courseName)) {
                    validCourse = true;
                    toBeAdded = c;
                }
            }
            if(toBeAdded == null) {
                System.out.println("\nThis Course either doesn't exist or there\nwas a spelling error. Try again.");
            }
            else if((toBeAdded.getCreditHours() + activeUser.getTotalRegisteredHours()) > 17) {
                System.out.println("\nAdding this course would exceed your course hour limit.");
            }
            else {
                if(validCourse) {  
                    for(Course c: activeUser.getRegisteredCourses()) {
                        if(c.getCourseName().toLowerCase().equals(courseName)) {
                            System.out.println("\nYou're already registered for this Course.");
                            validCourse = false;
                        }
                    }
                    for(Course c: activeUser.getTakenCourses()) {
                        if(c.getCourseName().toLowerCase().equals(courseName)) {
                            System.out.println("\nYou've already taken this Course in a previous semester.");
                            validCourse = false;
                        }
                    }
                    if(validCourse) {
                        activeUser.addRegisteredCourse(toBeAdded);
                        break;
                    }
                }
            }
        }
    }

    // This method in the Course Schedule menu allows the user to remove a course from their Schedule.
    // This method doesnt require confirmation because if the course was originally added it met all requirements.
    private static void removeCourse() {
        while(true) {
            boolean courseRemoved = false;
            String courseName = csMenuInputHelper();
            Course toBeRemoved = null;
            for(Course c: activeUser.getRegisteredCourses()) {
                if(c.getCourseName().toLowerCase().equals(courseName)) {
                    toBeRemoved = c;
                    courseRemoved = true;
                }
            }
            if(courseRemoved) {
                System.out.println();
                activeUser.removeRegisteredCourse(toBeRemoved);
                break;
            }
            System.out.print("\nThis Course doesn't exist or there was \na spelling error. Try again.\n");

        }

    }

    // This short method makes sure that the user doesnt change their username to one that already exists.
    private static Boolean usernameTakenHelper(String newUsername) {
        for(Users u: students) {
            if(newUsername.equals(u.getUsername())) {
                return true;
            }
        }
        return false;
    }
}
