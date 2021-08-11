package com.revature.registrar.pages;

import com.revature.registrar.models.Faculty;
import com.revature.registrar.models.Student;
import com.revature.registrar.models.User;
import com.revature.registrar.services.UserService;
import com.revature.registrar.util.AppState;
import com.revature.registrar.util.PageRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;

public class DashPage extends Page {
    private UserService userService;
    private AppState state;
    private final Logger logger = LogManager.getLogger(DashPage.class);

    public DashPage(BufferedReader consoleReader, PageRouter router, UserService userService, AppState state) {
        super("/dash", consoleReader, router);
        this.userService = userService;
        this.state = state;
    }

    /**
     * Renders a Dashboard Screen which presents different options depending on whether
     * the user is a Student or a Faculty member
     * @throws Exception
     */
    @Override
    public void render() throws Exception {
        System.out.println("--------------------");
        User currUser = userService.getCurrUser();
        System.out.println("Welcome " + currUser.getFirstName());
        if (currUser.isFaculty()) {
            Faculty fac = (Faculty) currUser;
            renderFaculty(fac);
        } else {
            Student stu = (Student) currUser;
            renderStudent(stu);
        }
    }

    /**
     * Renders the Faculty Dashboard Screen which presents options to Manage Classes, Create New Class,
     * or Logout
     * @param fac
     * @throws Exception
     */
    private void renderFaculty(Faculty fac) throws Exception {
        System.out.println("You're at the Faculty dashboard");
        System.out.print("1) Manage Classes\n2) Create New Class\n3) Logout\n>");
        String response = consoleReader.readLine();
        if(response.equals("1")) {
            router.switchPage("/myclasses");
        } else if (response.equals("2")) {
            router.switchPage("/register-class");
        } else if(response.equals("3")) {
            userService.setCurrUser(null);
            router.switchPage("/home");
            System.out.println("Logging out");
        } else {
            System.out.println("Invalid Input");
            return;
        }
    }

    /**
     * Renders the Student Dashboard Screen which presents options to Manage Classes, Discover Classes,
     * or Logout
     * @param stu
     * @throws Exception
     */
    private void renderStudent(Student stu) throws Exception {

        System.out.println("You're at the Student dashboard");
        System.out.print("1) Manage Classes\n2) Discover Classes\n3) Logout\n> ");
        String response = consoleReader.readLine();
        if(response.equals("1")) {
            router.switchPage("/myclasses");
        } else if (response.equals("2")) {
            router.switchPage("/discover");
        } else if(response.equals("3")) {
            userService.setCurrUser(null);
            router.switchPage("/home");
            System.out.println("Logging out");
        } else {
            System.out.println("Invalid Input");
            return;
        }
    }
}
