package com.revature.registrar.pages;

import com.revature.registrar.util.PageRouter;

import java.io.BufferedReader;

/**
 * Abstract class which is the blueprint for all Pages in our application
 */
public abstract class Page {
    //protected String name;
    protected String route;
    protected BufferedReader consoleReader;
    protected PageRouter router;

    public Page(String route, BufferedReader consoleReader, PageRouter router) {
        this.route = route;
        this.consoleReader = consoleReader;
        this.router = router;
    }

    public String getRoute() {
        return route;
    }

    public abstract void render() throws Exception;
}
