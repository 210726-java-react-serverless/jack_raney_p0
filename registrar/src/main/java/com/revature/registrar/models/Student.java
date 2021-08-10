package com.revature.registrar.models;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.bson.Document;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Student extends User{
    //Use set because we cannot have duplicate id's
    Set<ClassModel> classes = new HashSet<>(); //contains ids of registered classes OR should it store actual objects?

    public Student() {
        super();
    }

    public Student(String firstName, String lastName, String email, String username, String password) {
        super(firstName, lastName, email, username, password, false);
    }

    public Set<ClassModel> getClasses() {
        return classes;
    }

    public Set<Document> getClassesAsDoc() {
        Set<Document> docs = new HashSet<>();
        for(ClassModel classModel : classes) {
            Document doc = classModel.getAsDoc();
        }
        return docs;
    }

    public void addClass(ClassModel c) {
        classes.add(c);
    }

    public void removeClass(int id) {
        classes.remove(id);
    }
}
