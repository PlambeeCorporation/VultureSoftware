package com.plambeeco.dataaccess.repository;

import java.util.List;


public interface IRepository<T> {
    void add(T item);
    void update(T item);
    void remove(T item);
    T getById(int id);
    List<T> getAll();
}
