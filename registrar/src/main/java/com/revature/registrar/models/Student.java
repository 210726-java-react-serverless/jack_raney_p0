package com.revature.registrar.models;

import java.util.HashSet;
import java.util.Set;

public class Student extends User{
    //Use set because we cannot have duplicate id's
    Set<ClassModel> classes = new HashSet<>(); //contains ids of registered classes OR should it store actual objects?

    public Student(String firstName, String lastName, String email, String username, String password) {
        super(firstName, lastName, email, username, password, false);
    }

    public Set<ClassModel> getClasses() {
        return classes;
    }

    public void addClass(ClassModel c) {
        classes.add(c);
    }

    public void removeClass(int id) {
        classes.remove(id);
    }
}
