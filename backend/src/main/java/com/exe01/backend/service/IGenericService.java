package com.exe01.backend.service;

import com.exe01.backend.models.PagingModel;

import java.util.UUID;

public interface IGenericService<T> {
    T findById(UUID id);

    PagingModel getAll(Integer page, Integer limit);

    PagingModel findAllByStatusTrue(Integer page, Integer limit);

}
