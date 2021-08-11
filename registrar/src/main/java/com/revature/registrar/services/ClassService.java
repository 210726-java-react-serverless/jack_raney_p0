package com.revature.registrar.services;

import com.revature.registrar.exceptions.InvalidRequestException;
import com.revature.registrar.exceptions.ResourcePersistenceException;
import com.revature.registrar.models.ClassModel;
import com.revature.registrar.models.User;
import com.revature.registrar.repository.ClassModelRepo;
import com.revature.registrar.repository.UserRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClassService {
    private final ClassModelRepo classRepo;


    public ClassService(ClassModelRepo classRepo) {
        this.classRepo = classRepo;
    }

    public ClassModel getClassWithId(int id) {
        ClassModel result = classRepo.findById(id);
        if(result == null) {
            throw new InvalidRequestException("Invalid ID");
        } else {
            return classRepo.findById(id);
        }
    }

    //Refresh classModel with complete information
    public ClassModel refresh(ClassModel classModel) {
        return classRepo.findById(classModel.getId());
    }

    public List<ClassModel> getOpenClasses() {
        return classRepo.findOpenClasses();
    }

    public boolean delete(ClassModel classModel) {
        return classRepo.deleteById(classModel.getId());
    }

    public boolean update(ClassModel classModel) {
        return classRepo.update(classModel);
    }

    //Validate user input, store in ClassRepo and return ClassModel
    public ClassModel register(ClassModel classModel) throws RuntimeException{
        if(!isValid(classModel)) {
            throw new InvalidRequestException("Invalid user data provided");
        }

        //pass validated user to UserRepository
        classRepo.save(classModel);


        return classModel;
    }

    private boolean isValid(ClassModel classModel) {
        if(classModel == null) {
            return false;
        }

        Calendar current = Calendar.getInstance();
        if(classModel.getName() == null || classModel.getName().trim().equals("")) return false;
        if(classModel.getDescription() == null || classModel.getDescription().trim().equals("")) return false;
        if(classModel.getCapacity() <= 0) return false;
        if(classModel.getCapacity() < classModel.getStudents().size()) return false;

        //Open/Close Windows cannot be before the current time
        if(classModel.getOpenWindow() == null || classModel.getOpenWindow().getTimeInMillis() <= current.getTimeInMillis() ) return false;
        if(classModel.getCloseWindow() == null || classModel.getCloseWindow().getTimeInMillis() <= current.getTimeInMillis() ) return false;
        //Open has to be before the close
        if(classModel.getCloseWindow().getTimeInMillis() <= classModel.getOpenWindow().getTimeInMillis() ) return false;

        if(classModel.getStudents() == null) return false;
        if(classModel.getFaculty() == null) return false;

        //if a duplicate already exists in the db, reject
        if(classRepo.findById(classModel.getId()) != null) {
            throw new ResourcePersistenceException("Duplicate");
        }

        return true;
    }

}
