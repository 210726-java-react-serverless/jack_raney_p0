package com.revature.registrar.pages;

import com.revature.registrar.util.PageRouter;

import java.io.BufferedReader;

public class HomePage extends Page {
    public HomePage(BufferedReader consoleReader, PageRouter router) {
        super("/home", consoleReader, router);
    }

    @Override
    public void render() throws Exception {
        System.out.println("Welcome to the Class Registrar.");
        System.out.println(
                "1) Login\n" +
                "2) Register\n" +
                "3) Exit\n");

        String response = consoleReader.readLine();

        switch(response) {
            case "1":
                router.switchPage("/login");
                break;
            case "2":
                router.switchPage("/register");
                break;
            case "3":
                //TODO: FIX THIS
                System.out.println("Exiting app");
                System.exit(0);
            default:
                System.out.println("Invalid Input");

        }
    }
}
