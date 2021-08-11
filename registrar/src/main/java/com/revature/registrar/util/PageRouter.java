package com.revature.registrar.util;

import com.revature.registrar.exceptions.InvalidRouteException;
import com.revature.registrar.pages.Page;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Class which keeps track of the current Page and provides methods to switch Pages
 */
public class PageRouter {
    private Page currPage;
    //Using a hashtable so we can index in O(1)+ time
    private Dictionary<String, Page> pages = new Hashtable<String, Page>();

    /**
     * Adds a pages to the router
     * @param p
     */
    public void addPage(Page p) {
        pages.put(p.getRoute(), p);
    }

    /**
     * Switches the current pages to the Page associated with a given route
     * @param route
     */
    public void switchPage(String route) {
        Page p = pages.get(route);
        if(p == null) {
            throw new InvalidRouteException("bad route");
        } else {
            currPage = p;
        }
    }

    /**
     * Getter method to retrieve the current page
     * @return
     */
    public Page getCurrPage() {
        return currPage;
    }

}
