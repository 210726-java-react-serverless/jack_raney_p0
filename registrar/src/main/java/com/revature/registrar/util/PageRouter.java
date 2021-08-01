package com.revature.registrar.util;

import com.revature.registrar.exceptions.InvalidRouteException;
import com.revature.registrar.pages.Page;
import java.util.Dictionary;
import java.util.Hashtable;

public class PageRouter {
    private Page currPage;
    //Using a hashtable so we can index in O(1)+ time
    private Dictionary<String, Page> pages = new Hashtable<String, Page>();

    public void addPage(Page p) {
        pages.put(p.getRoute(), p);
    }

    public void switchPage(String route) {
        Page p = pages.get(route);
        if(p == null) {
            throw new InvalidRouteException("bad route");
        } else {
            currPage = p;
        }
    }

    public Page getCurrPage() {
        return currPage;
    }

}
