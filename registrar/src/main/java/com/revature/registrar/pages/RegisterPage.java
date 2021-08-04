package com.revature.registrar.pages;

import com.revature.registrar.App;
import com.revature.registrar.models.Faculty;
import com.revature.registrar.models.Student;
import com.revature.registrar.models.User;
import com.revature.registrar.services.UserService;
import com.revature.registrar.util.AppState;
import com.revature.registrar.util.PageRouter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RegisterPage extends Page {
    private UserService userService;
    private AppState state;

    public RegisterPage(BufferedReader consoleReader, PageRouter router, UserService userService, AppState state) {
        super("/register", consoleReader, router);
        this.userService = userService;
        this.state = state;
    }

    @Override
    public void render() throws Exception {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));


        System.out.println("Register as:\n1) Student\n2) Faculty");
        String response = consoleReader.readLine();
        boolean isFaculty;
        if(response.equals("1")) {
            isFaculty = false;
            System.out.println("Registering as Student");
        } else if (response.equals("2")) {
            isFaculty = true;
            System.out.println("Registering as Faculty");
        } else {
            System.out.println("Invalid Input");
            return;
        }

        System.out.println("Enter First Name: \n" + ">\n");
        String firstName = consoleReader.readLine();

        System.out.println("Enter Last Name: \n" + ">\n");
        String lastName = consoleReader.readLine();

        System.out.println("Enter Email: \n" + ">\n");
        String email = consoleReader.readLine();

        System.out.println("Enter Username: \n" + ">\n");
        String username = consoleReader.readLine();

        System.out.println("Enter Password: \n" + ">\n");
        String password = consoleReader.readLine();


        User newUser;
        if(isFaculty) {
            newUser = new Faculty(firstName, lastName, email, username, password);
        } else {
            newUser = new Student(firstName, lastName, email, username, password);
        }

        try {
            userService.register(newUser);
            router.switchPage("/dash");
            this.state.setCurrUser(newUser);
            //logger.info("New user created!\n" + newUser.toString());
        } catch(Exception e) {
            //logger.error("Invalid credentials");
            router.switchPage("/register");
        }
    }
}
