package com.revature.registrar.pages;

import com.revature.registrar.models.Faculty;
import com.revature.registrar.models.Student;
import com.revature.registrar.models.User;
import com.revature.registrar.services.UserService;
import com.revature.registrar.util.AppState;
import com.revature.registrar.util.PageRouter;

import java.io.BufferedReader;

public class DashPage extends Page {
    private UserService userService;
    private AppState state;

    public DashPage(BufferedReader consoleReader, PageRouter router, UserService userService, AppState state) {
        super("/dash", consoleReader, router);
        this.userService = userService;
        this.state = state;
    }

    @Override
    public void render() throws Exception {
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

    private void renderFaculty(Faculty fac) throws Exception {
        System.out.println("You're at the Faculty dashboard");
        /*
        1) See/Modify/Delete Current Classes
        2) Create New Class
        4) Logout
         */
    }

    private void renderStudent(Student stu) throws Exception {
        System.out.println("You're at the Student dashboard");
        /*
        1) See/Unenroll Current Classes
        2) Enroll in New Class
        3) Logout
         */

        System.out.println("1) Manage Classes\n2) Discover Classes\n3) Logout");
        String response = consoleReader.readLine();
        if(response.equals("1")) {
            router.switchPage("/myclasses");
        } else if (response.equals("2")) {
            System.out.println("Not yet implemented");
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
