package com.revature.registrar.pages;

import com.revature.registrar.models.User;
import com.revature.registrar.services.UserService;
import com.revature.registrar.util.AppState;
import com.revature.registrar.util.PageRouter;

import java.io.BufferedReader;

public class LoginPage extends Page {
    private UserService userService;
    private AppState state;

    public LoginPage(BufferedReader consoleReader, PageRouter router, UserService userService, AppState state) {
        super("/login" , consoleReader, router);
        this.userService = userService;
        this.state = state;

    }

    @Override
    public void render() throws Exception {
        System.out.println("Enter Username: \n" + ">");
        String username = consoleReader.readLine();

        System.out.println("Enter Password: \n" + ">");
        String password = consoleReader.readLine();

        User user = userService.login(username, password);
        if(user == null) {
            System.out.println("Login Failed");
            router.switchPage("/login");
        } else {
            System.out.println(user);
            router.switchPage("/dash");
        }
    }
}
