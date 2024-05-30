package com.exe01.backend.service;

import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;

import java.util.UUID;

public interface IGenericService<T> {
    T findById(UUID id) throws BaseException;

    PagingModel getAll(Integer page, Integer limit) throws BaseException;

    PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException;

}
