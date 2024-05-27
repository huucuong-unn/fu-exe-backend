package com.exe01.backend.service;

import com.exe01.backend.dto.UniversityDTO;
import com.exe01.backend.dto.request.university.CreateUniversityRequest;
import com.exe01.backend.dto.request.university.UpdateUniversityRequest;

import java.util.UUID;

public interface IUniversityService extends IGenericService<UniversityDTO>{

    public Boolean update(UUID id, UpdateUniversityRequest request);

    UniversityDTO create(CreateUniversityRequest request);

    public  Boolean delete(UUID id);

}
