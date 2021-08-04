package com.revature.registrar.util;

import com.revature.registrar.exceptions.InvalidRouteException;
import com.revature.registrar.models.User;
import com.revature.registrar.pages.*;
import com.revature.registrar.repository.UserRepository;
import com.revature.registrar.services.UserService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AppState {
    private boolean alive;
    private User currUser;
    private final PageRouter router;

    public AppState() {
        this.alive = true;
        this.router = new PageRouter();
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        UserRepository userRepo = new UserRepository();
        UserService userService = new UserService(userRepo);

        init(consoleReader, userService);
    }

    private void init(BufferedReader consoleReader, UserService userService) {

        router.addPage(new HomePage(consoleReader, router));
        router.addPage(new RegisterPage(consoleReader, router, userService, this));
        router.addPage(new LoginPage(consoleReader, router, userService, this));
        router.addPage(new DashPage(consoleReader, router, userService, this));
    }

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    public void startup() {
        router.switchPage("/home");

        while(alive) {
            try {
                router.getCurrPage().render();
            } catch (Exception e) {
                if (!(e instanceof InvalidRouteException)) {
                    e.printStackTrace();
                }
                alive = false;
            }
        }
    }

}
