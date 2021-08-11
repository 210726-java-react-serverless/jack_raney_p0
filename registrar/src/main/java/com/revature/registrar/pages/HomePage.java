package com.revature.registrar.pages;

import com.revature.registrar.util.PageRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;

public class HomePage extends Page {
    private final Logger logger = LogManager.getLogger(HomePage.class);
    public HomePage(BufferedReader consoleReader, PageRouter router) {
        super("/home", consoleReader, router);
    }

    /**
     * Renders the Home Page which is seen by users on startup. Users can choose to navigate to the Login or
     * Register pages from here. Also Users can Exit the application.
     * @throws Exception
     */
    @Override
    public void render() throws Exception {

        System.out.println("Welcome to the Class Registrar");
        System.out.print(
                "1) Login\n" +
                "2) Register\n" +
                "3) Exit\n> ");

        String response = consoleReader.readLine();

        switch(response) {
            case "1":
                router.switchPage("/login");
                break;
            case "2":
                router.switchPage("/register");
                break;
            case "3":
                //This is bad, dont do this
                System.out.println("Exiting app");
                System.exit(0);
            default:
                System.out.println("Invalid Input");

        }
    }
}
