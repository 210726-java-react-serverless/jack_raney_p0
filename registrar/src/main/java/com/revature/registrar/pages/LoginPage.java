package com.revature.registrar.pages;

import com.revature.registrar.models.User;
import com.revature.registrar.services.UserService;
import com.revature.registrar.util.AppState;
import com.revature.registrar.util.PageRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;

public class LoginPage extends Page {
    private UserService userService;
    private AppState state;
    private final Logger logger = LogManager.getLogger(LoginPage.class);

    public LoginPage(BufferedReader consoleReader, PageRouter router, UserService userService, AppState state) {
        super("/login" , consoleReader, router);
        this.userService = userService;
        this.state = state;

    }

    /**
     * Renders the Login Page where users enter their credentials for authorization
     * @throws Exception
     */
    @Override
    public void render() throws Exception {
        System.out.println("--------------------");
        System.out.print("Enter Username: \n" + "> ");
        String username = consoleReader.readLine();

        System.out.print("Enter Password: \n" + "> ");
        String password = consoleReader.readLine();

        User user = userService.login(username, password);
        if(user == null) {
            System.out.println("Login Failed");
            router.switchPage("/home");
        } else {
            router.switchPage("/dash");
        }
    }
}
