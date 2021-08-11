package com.revature.registrar.pages;

import com.revature.registrar.models.ClassModel;
import com.revature.registrar.models.Faculty;
import com.revature.registrar.models.Student;
import com.revature.registrar.models.User;
import com.revature.registrar.services.ClassService;
import com.revature.registrar.services.UserService;
import com.revature.registrar.util.AppState;
import com.revature.registrar.util.CalendarBuilder;
import com.revature.registrar.util.PageRouter;
import jdk.nashorn.internal.runtime.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;

public class RegisterClassPage extends Page {

    private ClassService classService;
    private UserService userService;
    private AppState state;
    private final Logger logger = LogManager.getLogger(RegisterClassPage.class);

    public RegisterClassPage(BufferedReader consoleReader, PageRouter router, ClassService classService, UserService userService, AppState state) {
        super("/register-class", consoleReader, router);
        this.classService = classService;
        this.userService = userService;
        this.state = state;
    }

    /**
     * Renders the Register Class Screen which is only accessible by Faculty members
     * Allows Faculty to create a new class.
     * The new class will be added to the db if all entries are valid.
     * @throws Exception
     */
    @Override
    public void render() throws Exception {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("--------------------");
        System.out.println("Register a new class here!:\n1) Continue\n2) Return to Dashboard");
        String response = consoleReader.readLine();
        if(response.equals("2")) {
            router.switchPage("/dash");
            return;
        } else if (!response.equals("1")){
            System.out.println("Invalid Input");
            return;
        }

        System.out.println("Enter Name: \n" + ">\n");
        String name = consoleReader.readLine();

        System.out.println("Enter Description: \n" + ">\n");
        String description = consoleReader.readLine();

        System.out.println("Enter Maximum Student Count: \n" + ">\n");
        int capacity = Integer.parseInt(consoleReader.readLine());

        CalendarBuilder cb = new CalendarBuilder(consoleReader);
        System.out.println("Configuring Registration Window: \n");
        System.out.println("Open Window: \n");
        Calendar openDate = cb.build();
        System.out.println("Close Window");
        Calendar closeDate = cb.build();

        Set<Faculty> facultySet = new HashSet<>();
        facultySet.add((Faculty) userService.getCurrUser());
        ClassModel classModel = new ClassModel(name, description, capacity, openDate, closeDate, facultySet);
        try {
            classService.register(classModel);
            ((Faculty) userService.getCurrUser()).addClass(classModel);
            userService.update(userService.getCurrUser());
            router.switchPage("/dash");
            //logger.info("New user created!\n" + newUser.toString());
        } catch(Exception e) {
            //logger.error("Invalid credentials");
            logger.error(e.getStackTrace() + "\n");
            System.out.println("Invalid credentials");
        }
    }
}
