package com.exe01.backend.service;

import com.exe01.backend.dto.MajorDTO;
import com.exe01.backend.dto.request.major.CreateMajorRequest;
import com.exe01.backend.dto.request.major.UpdateMajorRequest;

import java.util.UUID;

public interface IMajorService extends IGenericService<MajorDTO>{

    public MajorDTO create(CreateMajorRequest request);

    public Boolean update(UUID id, UpdateMajorRequest request);

    public  Boolean delete(UUID id);

}
