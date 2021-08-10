package com.revature.registrar.util;

import com.revature.registrar.exceptions.InvalidRouteException;
import com.revature.registrar.models.User;
import com.revature.registrar.pages.*;
import com.revature.registrar.repository.ClassModelRepo;
import com.revature.registrar.repository.UserRepository;
import com.revature.registrar.services.ClassService;
import com.revature.registrar.services.UserService;
import sun.reflect.generics.repository.ClassRepository;

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
        ClassModelRepo classRepo = new ClassModelRepo();
        UserService userService = new UserService(userRepo);
        ClassService classService = new ClassService(classRepo);

        init(consoleReader, userService, classService);
    }

    private void init(BufferedReader consoleReader, UserService userService, ClassService classService) {

        router.addPage(new HomePage(consoleReader, router));
        router.addPage(new RegisterPage(consoleReader, router, userService, this));
        router.addPage(new LoginPage(consoleReader, router, userService, this));
        router.addPage(new DashPage(consoleReader, router, userService, this));
        router.addPage(new MyClassesPage(consoleReader, router, userService, this));
        router.addPage(new RegisterClassPage(consoleReader, router, classService, userService, this));
        router.addPage(new DiscoverClassesPage(consoleReader, router, classService, userService, this));
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
