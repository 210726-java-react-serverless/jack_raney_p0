package com.revature.registrar.pages;

import com.revature.registrar.util.PageRouter;

import java.io.BufferedReader;

public class RegisterPage extends Page {

    public RegisterPage(BufferedReader consoleReader, PageRouter router) {
        super("/register", consoleReader, router);
    }

    @Override
    public void render() throws Exception {
        System.out.println("YOU'RE AT REGISTER");
    }
}
