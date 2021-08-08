package com.revature.registrar.pages;

import com.revature.registrar.models.ClassModel;
import com.revature.registrar.models.Faculty;
import com.revature.registrar.models.Student;
import com.revature.registrar.models.User;
import com.revature.registrar.services.UserService;
import com.revature.registrar.util.AppState;
import com.revature.registrar.util.PageRouter;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MyClassesPage extends Page {
    private UserService userService;
    private AppState state;

    public MyClassesPage(BufferedReader consoleReader, PageRouter router, UserService userService, AppState state) {
        super("/myclasses", consoleReader, router);
        this.userService = userService;
        this.state = state;
    }

    @Override
    public void render() throws Exception {
        User currUser = userService.getCurrUser();
        System.out.println("Welcome " + currUser.getFirstName());
        if (currUser.isFaculty()) {
            Faculty fac = (Faculty) currUser;
            renderFaculty(fac);
        } else {
            Student stu = (Student) currUser;
            renderStudent(stu);
        }
    }

    private void renderFaculty(Faculty fac) throws Exception {
        //List fac.classes
        Set<Faculty> facSet = new HashSet<>();
        facSet.add(fac);
        ClassModel classModel = new ClassModel("Intro to Programming", 20, null, null, facSet);
        fac.addClass(classModel);
        System.out.println("Enrolled Classes");
        for(ClassModel c : fac.getClasses()) {
            Set<String> facLastNames = c.getFaculty().stream().map(faculty -> faculty.getLastName()).collect(Collectors.toSet());
            String unsigned = Integer.toUnsignedString(c.getId()); //Conversion fo readability (no negatives)
            System.out.println(unsigned + " | " + c.getName() + " | " + facLastNames + " | " + "(" + c.getStudents().size() + "/" + c.getCapacity() + ")");
        }

        System.out.println("1) Update Class \n2) Delete Class\n3) Dashboard\n4) Logout");
        String response = consoleReader.readLine();
        if(response.equals("1")) {
            System.out.println("Enter Class id");
            int id = Integer.parseUnsignedInt(consoleReader.readLine());
            //TODO: Update class
        } else if(response.equals("2")) {
            int id = Integer.parseUnsignedInt(consoleReader.readLine());
            //TODO: Delete class

        } else if (response.equals("3")) {
            router.switchPage("/dash");
        } else if(response.equals("4")) {
            userService.setCurrUser(null);
            router.switchPage("/home");
            System.out.println("Logging out");
        } else {
            System.out.println("Invalid Input");
            return;
        }
    }

    private void renderStudent(Student stu) throws Exception {
        //List stu.classes
        Faculty test = new Faculty("Test", "Testerson", "test@email.com", "testy", "pass");
        Set<Faculty> fac = new HashSet<>();
        fac.add(test);
        ClassModel classModel = new ClassModel("Intro to Programming", 20, null, null, fac);
        classModel.addStudent(stu);
        stu.addClass(classModel);
        System.out.println("Enrolled Classes");
        for(ClassModel c : stu.getClasses()) {
            Set<String> facLastNames = c.getFaculty().stream().map(faculty -> faculty.getLastName()).collect(Collectors.toSet());
            String unsigned = Integer.toUnsignedString(c.getId()); //Conversion fo readability (no negatives)
            System.out.println(unsigned + " | " + c.getName() + " | " + facLastNames + " | " + "(" + c.getStudents().size() + "/" + c.getCapacity() + ")");
        }

        System.out.println("1) Unenroll \n2) Return to Dashboard\n3) Logout");
        String response = consoleReader.readLine();
        if(response.equals("1")) {
            System.out.println("Enter Class id");
            int id = Integer.parseUnsignedInt(consoleReader.readLine());
            //TODO: Remove from class if within registration window

        } else if (response.equals("2")) {
            router.switchPage("/dash");
        } else if(response.equals("3")) {
            userService.setCurrUser(null);
            router.switchPage("/home");
            System.out.println("Logging out");
        } else {
            System.out.println("Invalid Input");
            return;
        }
    }
}
