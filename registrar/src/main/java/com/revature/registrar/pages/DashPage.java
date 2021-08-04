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
        User currUser = state.getCurrUser();
        System.out.println("Welcome " + currUser.getFirstName());
        if (currUser.isFaculty()) {
            Faculty fac = (Faculty) currUser;
            renderFaculty(fac);
        } else {
            Student stu = (Student) currUser;
            renderStudent(stu);
        }
    }

    private void renderFaculty(Faculty fac) {
        System.out.println("You're at the Faculty dashboard");
    }

    private void renderStudent(Student stu) {
        System.out.println("You're at the Student dashboard");
    }
}
