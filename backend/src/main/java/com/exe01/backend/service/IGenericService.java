package com.exe01.backend.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IGenericService<T> {
    T findById(Long id);

    List<T>  findAll();

    Boolean checkExist(Long id);

}
