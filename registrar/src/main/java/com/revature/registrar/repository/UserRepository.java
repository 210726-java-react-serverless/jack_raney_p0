package com.revature.registrar.repository;

import com.revature.registrar.models.User;

public class UserRepository implements CrudRepository {
    @Override
    public Object findById(int id) {
        return null;
    }

    @Override
    public Object save(Object newResource) {
        return null;
    }

    @Override
    public boolean update(Object updatedResource) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    public User findUserByCredentials(String username, String password) {
        return null;
    }
}
