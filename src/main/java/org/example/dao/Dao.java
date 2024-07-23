package org.example.dao;

import java.util.List;

public interface Dao<K, E> {

    void save(E entity);
    E findById(K id);
    List<E> findAll();
    void update(K id, E entity);
    void delete(K id);

}
