package com.revature.registrar.pages;

import com.revature.registrar.exceptions.CapacityReachedException;
import com.revature.registrar.models.ClassModel;
import com.revature.registrar.models.Student;
import com.revature.registrar.services.ClassService;
import com.revature.registrar.services.UserService;
import com.revature.registrar.util.AppState;
import com.revature.registrar.util.PageRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DiscoverClassesPage extends Page {
    private ClassService classService;
    private UserService userService;
    private AppState state;
    private final Logger logger = LogManager.getLogger(DiscoverClassesPage.class);

    public DiscoverClassesPage(BufferedReader consoleReader, PageRouter router, ClassService classService, UserService userService, AppState state) {
        super("/discover", consoleReader, router);
        this.classService = classService;
        this.userService = userService;
        this.state = state;
    }

    /**
     * Renders the Discover Screen which displays courses which are open for enrollment,
     * accessible by Students only
     * @throws Exception
     */
    @Override
    public void render() throws Exception {
        System.out.println("--------------------");
        System.out.println("Welcome to the Discovery Page");
        System.out.println("Listing Open Courses:");
        List<ClassModel> classes = new ArrayList<>();
        classes = classService.getOpenClasses();
        if(classes == null) {
            System.out.println("***No Open Courses***");
            router.switchPage("/dash");
            return;
        }

        int count = 0;
        for(ClassModel c : classes) {
            Student currUser = (Student) userService.getCurrUser();
            if(!currUser.isInClasses(c)) {
                Set<String> facLastNames = c.getFaculty().stream().map(faculty -> faculty.getLastName()).collect(Collectors.toSet());
                String unsigned = Integer.toUnsignedString(c.getId()); //Conversion for readability (no negatives)
                System.out.println(unsigned + " | " + c.getName() + " | " + facLastNames + " | " + "(" + c.getStudents().size() + "/" + c.getCapacity() + ")");
                count++;
            }
        }
        if(count == 0) {
            System.out.println("No Open Classes");
        }

        System.out.println("1) Enroll\n2) Return to Dashboard\n3) Logout");
        String response = consoleReader.readLine();
        if(response.equals("1")) {
            try {
                enroll();
            } catch(CapacityReachedException cre) {
                System.out.println("Capacity Reached");
                logger.error("Capacity Reached\n");
            }
        } else if (response.equals("2")) {
            router.switchPage("/dash");
        } else if(response.equals("3")) {
            userService.setCurrUser(null);
            router.switchPage("/home");
            System.out.println("Logging Out");
        } else {
            System.out.println("Invalid Input");
            return;
        }
    }

    /**
     * Allows the User to select a class to enroll in.
     * Validates and updates the db accordingly
     * @throws Exception
     */
    private void enroll() throws Exception {
        System.out.println("Enter Course Id To Enroll In: ");
        String unsigned = consoleReader.readLine();
        int id = 0;
        try {
            id = Integer.parseUnsignedInt(unsigned);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid Input");
            return;
        }


        ClassModel classModel = null;
        try {
            classModel = classService.getClassWithId(id);
        } catch (Exception e) {
            System.out.println("Invalid Class ID");
            router.switchPage("/discover");
            return;
        }

        Student curr = (Student)userService.getCurrUser();
        if(curr.isInClasses(classModel)) {
            System.out.println("ALREADY ENROLLED");
            return;
        }

        classModel.addStudent((Student)userService.getCurrUser());
        ((Student) userService.getCurrUser()).addClass(classModel);

        //Need to persist these changes to the db with UPDATE
        classService.update(classModel);
        userService.update(userService.getCurrUser());
    }
}
