package com.revature.registrar.models;


import com.revature.registrar.exceptions.CapacityReachedException;
import com.revature.registrar.exceptions.InvalidUserTypesException;

import java.util.Set;

public class ClassModel {
    private final int id;
    private final String name;
    private int capacity;
    private Set<Student> students;
    private Set<Faculty> faculty; //Could have multiple faculty members per class

    public ClassModel(String name) {
        this.name = name;
        this.id = name.hashCode();
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Set<Faculty> getFaculty() {
        return faculty;
    }

    public void addUser(User user) {
        if(user.isFaculty()) {
            Faculty fac = (Faculty)user;
            addFaculty(fac);
        } else {
            Student stu = (Student) user;
            addStudent(stu);
        }
    }

    public void addFaculty(Faculty fac) {
        faculty.add(fac);
    }

    public void addStudent(Student stu) {
        if(students.size() < capacity) {
            students.add(stu);
        } else {
            //No more room in the class! Throw an exception
            throw new CapacityReachedException("Class capacity for " + this.name + " is reached");
        }
    }

    //Remove the user from the class. There must be at least 1 faculty member per class.
    public void removeUser(User user) {
        if(user.isFaculty()) {
            if(faculty.size() > 1) {
                Faculty fac = (Faculty) user;
                removeFac(fac);
            }
        } else {
            Student stu = (Student) user;
            removeStudent(stu);
        }
    }

    public void removeFac(Faculty fac) {
        faculty.remove(fac);
    }

    public void removeStudent(Student stu) {
        students.remove(stu);
    }

    public void switchUser(User existing, User swap) {
        if(existing.isFaculty() != swap.isFaculty()) {
            //Cannot swap users of different types
            throw new InvalidUserTypesException("Cannot swap users of different types (both must be Student or Faculty)");
        }
    }
}