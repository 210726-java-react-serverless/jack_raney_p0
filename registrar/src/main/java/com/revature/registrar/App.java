package com.revature.registrar;

import com.revature.registrar.util.AppState;

/**
 * Basic application driver
 */
public class App {
    //private static AppState app = new AppState();

    public static void main(String[] args) {
        AppState app = new AppState();
        app.startup();
    }
}
