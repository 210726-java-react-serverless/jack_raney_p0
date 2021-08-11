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

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MyClassesPage extends Page {
    private UserService userService;
    private ClassService classService;
    private AppState state;

    public MyClassesPage(BufferedReader consoleReader, PageRouter router, UserService userService, ClassService classService, AppState state) {
        super("/myclasses", consoleReader, router);
        this.userService = userService;
        this.classService = classService;
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
        System.out.println("Registered Classes");
        if(fac.getClasses().size() == 0) {
            System.out.println("No Classes Registered");
            router.switchPage("/dash");
            return;
        }
        for(ClassModel c : fac.getClasses()) {
            c = classService.refresh(c); //Go fill out with most recent data
            Set<String> facLastNames = c.getFaculty().stream().map(faculty -> faculty.getLastName()).collect(Collectors.toSet());
            String unsigned = Integer.toUnsignedString(c.getId()); //Conversion fo readability (no negatives)
            System.out.println(unsigned + " | " + c.getName() + " | " + facLastNames + " | " + "(" + c.getStudents().size() + "/" + c.getCapacity() + ")");
        }

        System.out.println("1) Update Class \n2) Delete Class\n3) Dashboard\n4) Logout");
        String response = consoleReader.readLine();
        if(response.equals("1")) {
            //TODO: Update class
            updateClass();
        } else if(response.equals("2")) {
            deleteClass();
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

    private void deleteClass() throws Exception{
        System.out.println("Enter Course Id To Delete: ");
        String unsigned = consoleReader.readLine();
        int id = Integer.parseUnsignedInt(unsigned);
        ClassModel classModel = null;

        try {
            classModel = classService.getClassWithId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Delete class from db
        classService.delete(classModel);
        //Delete class from all Users in DB
        userService.deleteClassFromAll(classModel);

    }

    private void updateClass() throws Exception {
        System.out.println("Enter Course Id To Update: ");
        String unsigned = consoleReader.readLine();
        int id = Integer.parseUnsignedInt(unsigned);
        ClassModel classModel = null;

        try {
            classModel = classService.getClassWithId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("1) Update Description\n" +
                "2) Update Open Window\n3) Update Close Window\n4) Update Capacity\n" +
                "5) Return To My Classes");
        String response = consoleReader.readLine();

        if(response.equals("1")) {
            System.out.println("Enter New Description");
            response = consoleReader.readLine();
            classModel.setDescription(response);
        } else if(response.equals("2")) {
            CalendarBuilder cb = new CalendarBuilder(consoleReader);
            classModel.setOpenWindow(cb.build());
        } else if(response.equals("3")) {
            CalendarBuilder cb = new CalendarBuilder(consoleReader);
            classModel.setCloseWindow(cb.build());
        } else if(response.equals("4")) {
            System.out.println("Enter New Capacity: \n" + ">\n");
            int capacity = Integer.parseInt(consoleReader.readLine());
            classModel.setCapacity(capacity);
        } else if(response.equals("5")) {
            return;
        } else {
            System.out.println("Invalid Input");
            return;
        }
        classService.update(classModel);
    }

    private void renderStudent(Student stu) throws Exception {
        //List stu.classes
        System.out.println("Enrolled Classes");
        for(ClassModel c : stu.getClasses()) {
            //Get the full class with Students and Faculty data.
            c = classService.getClassWithId(c.getId());
            Set<String> facLastNames = c.getFaculty().stream().map(faculty -> faculty.getLastName()).collect(Collectors.toSet());
            String unsigned = Integer.toUnsignedString(c.getId()); //Conversion fo readability (no negatives)
            System.out.println(unsigned + " | " + c.getName() + " | " + facLastNames + " | " + "(" + c.getStudents().size() + "/" + c.getCapacity() + ")");
        }

        System.out.println("1) Unenroll \n2) Return to Dashboard\n3) Logout");
        String response = consoleReader.readLine();
        if(response.equals("1")) {
            unenroll();
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

    //TODO: Check if within window
    private void unenroll() throws Exception {
        System.out.println("Enter Course Id To Unenroll From: ");
        String unsigned = consoleReader.readLine();
        int id = Integer.parseUnsignedInt(unsigned);
        ClassModel classModel = null;

        try {
            classModel = classService.getClassWithId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(classModel);
        Student curr = (Student)userService.getCurrUser();
        if(curr.isInClasses(classModel)) {
            classModel.removeStudent(curr);
            curr.removeClass(classModel);

            //persist changes
            classService.update(classModel);
            userService.update(curr);
            return;
        } else {
            System.out.println("Invalid Course");
        }
    }
}
