package com.revature.registrar.pages;

import com.revature.registrar.models.ClassModel;
import com.revature.registrar.models.Faculty;
import com.revature.registrar.models.Student;
import com.revature.registrar.models.User;
import com.revature.registrar.services.ClassService;
import com.revature.registrar.services.UserService;
import com.revature.registrar.util.AppState;
import com.revature.registrar.util.PageRouter;
import jdk.nashorn.internal.runtime.Context;

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

    public RegisterClassPage(BufferedReader consoleReader, PageRouter router, ClassService classService, UserService userService, AppState state) {
        super("/register-class", consoleReader, router);
        this.classService = classService;
        this.userService = userService;
        this.state = state;
    }

    @Override
    public void render() throws Exception {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Register a new class here!:\n1) Continue\n2) Return to Dashboard");
        String response = consoleReader.readLine();
        if(response.equals("2")) {
            router.switchPage("/dash");
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

        System.out.println("Configuring Registration Window: \n");
        System.out.println("Open Window: \n");
        Calendar openDate = buildCalendar();
        System.out.println("Close Window");
        Calendar closeDate = buildCalendar();

        Set<Faculty> facultySet = new HashSet<>();
        facultySet.add((Faculty) userService.getCurrUser());
        ClassModel classModel = new ClassModel(name, description, capacity, openDate, closeDate, facultySet);
        try {
            classService.register(classModel);
            router.switchPage("/dash");
            //logger.info("New user created!\n" + newUser.toString());
        } catch(Exception e) {
            //logger.error("Invalid credentials");
            e.printStackTrace();
            System.out.println("Invalid credentials");
        }
    }

    private Calendar buildCalendar() throws Exception {
        System.out.println("Date (MM/DD/YYYY): \n" + ">\n");
        String response = consoleReader.readLine();

        String[] vals = response.split("/");
        int month = 0;
        int day = 0;
        int year = 0;
        try {
            month = Integer.parseInt(vals[0]);
            day = Integer.parseInt(vals[1]);
            year = Integer.parseInt(vals[2]);
        } catch (Exception e) {
            return null;
        }

        if(month <= 0 || month > 12) return null;
        if(day <= 0 || day > 31) return null;
        if(year < 2021) return null;

        System.out.println("Time (HH:MM):");

        response = consoleReader.readLine();

        String[] vals2 = response.split(":");
        int hour;
        int minute;
        try {
            hour = Integer.parseInt(vals2[0]);
            minute = Integer.parseInt(vals2[1]);
        } catch (Exception e) {
            return null;
        }

        if(hour <= 0 || hour >= 24) return null;
        if(minute <= 0 || minute >= 60) return null;


        Calendar date = new Calendar.Builder()
                .setCalendarType("iso8601")
                .setDate(year, month - 1, day)
                .setTimeOfDay(hour, minute,0)
                .build();

        return date;
    }
}
