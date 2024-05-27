package com.exe01.backend.converter;

import com.exe01.backend.dto.UniversityDTO;
import com.exe01.backend.entity.University;

public class UniversityConverter {
    public static UniversityDTO toDTO(University university) {

        UniversityDTO universityDTO = new UniversityDTO();
        universityDTO.setId(university.getId());
        universityDTO.setName(university.getName());
        universityDTO.setAddress(university.getAddress());
        universityDTO.setStatus(university.getStatus());

        return universityDTO;

    }

}
