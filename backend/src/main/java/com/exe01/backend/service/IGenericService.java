package com.exe01.backend.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IGenericService<T> {
    T findById(Long id);

    List<T> findAllByStatusTrue(int page, int limit);
    List<T>  findAll();

    ResponseEntity<?> save(T t);

    ResponseEntity<?> changeStatus(Long id);

    Boolean checkExist(Long id);

}
