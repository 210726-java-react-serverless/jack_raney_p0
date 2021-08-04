package com.revature.registrar.repository;

public interface CrudRepository<E> {

    E findById(int id);
    E save(E newResource);
    boolean update(E updatedResource);
    boolean deleteById(int id);
}
