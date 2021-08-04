package com.revature.registrar.services;

import com.revature.registrar.exceptions.InvalidRequestException;
import com.revature.registrar.exceptions.ResourcePersistenceException;
import com.revature.registrar.models.User;
import com.revature.registrar.repository.UserRepository;

public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
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
        return userRepo.findUserByCredentials(username, password);
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
