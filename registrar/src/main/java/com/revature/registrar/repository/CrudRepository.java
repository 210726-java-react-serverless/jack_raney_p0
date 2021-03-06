package com.revature.registrar.repository;


/**
 *  Basic interface that exposes simple CRUD operations
 * @param <E>
 */
public interface CrudRepository<E> {

    E findById(int id);
    E save(E newResource);
    boolean update(E updatedResource);
    boolean deleteById(int id);
}
