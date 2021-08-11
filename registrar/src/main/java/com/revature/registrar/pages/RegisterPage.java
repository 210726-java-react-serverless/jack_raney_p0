package com.revature.registrar.pages;

import com.revature.registrar.App;
import com.revature.registrar.models.Faculty;
import com.revature.registrar.models.Student;
import com.revature.registrar.models.User;
import com.revature.registrar.services.UserService;
import com.revature.registrar.util.AppState;
import com.revature.registrar.util.PageRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RegisterPage extends Page {
    private UserService userService;
    private AppState state;
    private final Logger logger = LogManager.getLogger(RegisterPage.class);

    public RegisterPage(BufferedReader consoleReader, PageRouter router, UserService userService, AppState state) {
        super("/register", consoleReader, router);
        this.userService = userService;
        this.state = state;
    }

    /**
     * Renders the Register Page where users can choose to register a Faculty or Student account.
     * Users will pass in a username and password and an account will be created if entries are valid.
     * @throws Exception
     */
    @Override
    public void render() throws Exception {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("--------------------");
        System.out.print("Register as:\n1) Student\n2) Faculty\n3) Return Home\n> ");
        String response = consoleReader.readLine();
        boolean isFaculty;
        if(response.equals("1")) {
            isFaculty = false;
            System.out.println("--------------------");
            System.out.println("Registering as Student");
        } else if (response.equals("2")) {
            isFaculty = true;
            System.out.println("--------------------");
            System.out.println("Registering as Faculty");
        } else if (response.equals("3")) {
            router.switchPage("/home");
            return;
        } else {
            System.out.println("Invalid Input");
            return;
        }

        System.out.print("Enter First Name: \n" + "> ");
        String firstName = consoleReader.readLine();

        System.out.print("Enter Last Name: \n" + "> ");
        String lastName = consoleReader.readLine();

        System.out.print("Enter Email: \n" + "> ");
        String email = consoleReader.readLine();

        System.out.print("Enter Username: \n" + "> ");
        String username = consoleReader.readLine();

        System.out.print("Enter Password: \n" + "> ");
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
            this.userService.setCurrUser(newUser);
            //logger.info("New user created!\n" + newUser.toString());
        } catch(Exception e) {
            //logger.error("Invalid credentials");
            System.out.println("Invalid Credentials");
            router.switchPage("/register");
        }
    }
}
