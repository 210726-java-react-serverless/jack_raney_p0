package com.revature.registrar.util;

import com.revature.registrar.pages.Page;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AppState {
    private boolean alive;
    private final PageRouter router;

    public AppState() {
        this.alive = true;
        this.router = new PageRouter();
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        //UserRepository userRepo = new UserRepository();
        //UserService userService = new UserService(userRepo);

        init(consoleReader);


    }

    private void init(BufferedReader consoleReader) {

    }

    public void startup() {
        router.switchPage("/home");

        while(alive) {
            try {
                router.getCurrPage().render();
            } catch (Exception e) {
                //if (!(e instanceof InvalidRouteException)) {
                //    e.printStackTrace();
                //}
                alive = false;
            }
        }
    }

}
