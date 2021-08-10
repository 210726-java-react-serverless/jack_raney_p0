package com.revature.registrar.pages;

import com.revature.registrar.models.ClassModel;
import com.revature.registrar.models.Student;
import com.revature.registrar.services.ClassService;
import com.revature.registrar.services.UserService;
import com.revature.registrar.util.AppState;
import com.revature.registrar.util.PageRouter;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DiscoverClassesPage extends Page {
    private ClassService classService;
    private UserService userService;
    private AppState state;

    public DiscoverClassesPage(BufferedReader consoleReader, PageRouter router, ClassService classService, UserService userService, AppState state) {
        super("/discover", consoleReader, router);
        this.classService = classService;
        this.userService = userService;
        this.state = state;
    }

    @Override
    public void render() throws Exception {
        System.out.println("Welcome to the Discovery Page");
        System.out.println("Listing Open Courses:");
        List<ClassModel> classes = new ArrayList<>();
        classes = classService.getOpenClasses();
        if(classes == null) {
            System.out.println("No Open Courses");
            router.switchPage("/dash");
            return;
        }

        for(ClassModel c : classes) {
            Student currUser = (Student) userService.getCurrUser();
            if(currUser.getClasses().contains(c)) {
                System.out.println("User already enrolled in " + c.getName());
            }
            Set<String> facLastNames = c.getFaculty().stream().map(faculty -> faculty.getLastName()).collect(Collectors.toSet());
            String unsigned = Integer.toUnsignedString(c.getId()); //Conversion fo readability (no negatives)
            System.out.println(unsigned + " | " + c.getName() + " | " + facLastNames + " | " + "(" + c.getStudents().size() + "/" + c.getCapacity() + ")");
        }

        System.out.println("1) Enroll\n2) Return to Dashboard\n3) Logout");
        String response = consoleReader.readLine();
        if(response.equals("1")) {
            //TODO: Implement Enrollment
            System.out.println("Not Implemented");

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
