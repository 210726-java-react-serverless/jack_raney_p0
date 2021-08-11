package com.revature.registrar.services;

import com.revature.registrar.exceptions.InvalidRequestException;
import com.revature.registrar.exceptions.ResourcePersistenceException;
import com.revature.registrar.models.ClassModel;
import com.revature.registrar.models.Faculty;
import com.revature.registrar.models.Student;
import com.revature.registrar.models.User;
import com.revature.registrar.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepo;

    private User currUser;

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public boolean update(User user) {
        return userRepo.update(user);
    }


    public User getUserWithId(int id) {
        User result = userRepo.findById(id);
        if(result == null) {
            throw new InvalidRequestException("Invalid ID");
        } else {
            return userRepo.findById(id);
        }
    }

    public boolean deleteClassFromAll(ClassModel classModel) throws RuntimeException {
        List<User> users = userRepo.findWithClass(classModel.getId());
        for(User user : users) {
            if (user.isFaculty()) {
                Faculty fac = (Faculty) user;
                fac.removeClass(classModel);
            } else {
                Student stu = (Student) user;
                stu.removeClass(classModel);
            }
            update(user);
        }

        return true;
    }

    //Refresh classModel with complete information
    public User refresh(User user) {
        return userRepo.findById(user.getId());
    }

    //Validate user input, store in UserRepo and return AppUser with repo_id
    public User register(User user) throws RuntimeException{
        if(!isValid(user)) {
            throw new InvalidRequestException("Invalid user data provided");
        }

        //pass validated user to UserRepository
        userRepo.save(user);

        return user;
    }

    //Return the AppUser associated with a given username and password
    public User login(String username, String password) {
        User user = userRepo.findUserByCredentials(username, password);
        setCurrUser(user);
        return user;
    }

    private boolean isValid(User user) {
        if(user == null) {
            return false;
        }
        if(user.getFirstName() == null || user.getFirstName().trim().equals("")) return false;
        if(user.getLastName() == null || user.getLastName().trim().equals("")) return false;
        if(user.getPassword() == null || user.getPassword().trim().equals("")) return false;
        if(user.getEmail() == null || user.getEmail().trim().equals("")) return false;
        if(user.getUsername() == null || user.getUsername().trim().equals("")) return false;

        //if a duplicate already exists in the db, reject
        if(userRepo.findById(user.getId()) != null) {
            throw new ResourcePersistenceException("Duplicate");
        }

        return true;
    }

}
