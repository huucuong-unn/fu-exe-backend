package com.exe01.backend.converter;

import com.exe01.backend.dto.UniversityDTO;
import com.exe01.backend.dto.response.university.UniversityDropDownListResponse;
import com.exe01.backend.entity.University;

public class UniversityConverter {
    public static UniversityDTO toDTO(University university) {

        UniversityDTO universityDTO = new UniversityDTO();
        universityDTO.setId(university.getId());
        universityDTO.setName(university.getName());
        universityDTO.setAddress(university.getAddress());
        universityDTO.setStatus(university.getStatus());
        universityDTO.setCreatedDate(university.getCreatedDate());
        universityDTO.setModifiedDate(university.getModifiedDate());
        universityDTO.setCreatedBy(university.getCreatedBy());
        universityDTO.setModifiedBy(university.getModifiedBy());

        return universityDTO;

    }

    public static University toEntity(UniversityDTO universityDTO) {

        University university = new University();
        university.setId(universityDTO.getId());
        university.setName(universityDTO.getName());
        university.setAddress(universityDTO.getAddress());
        university.setStatus(universityDTO.getStatus());

        return university;
    }

    public static UniversityDropDownListResponse toDropDownListResponse(University university) {
        UniversityDropDownListResponse response = new UniversityDropDownListResponse();
        response.setId(university.getId());
        response.setName(university.getName());
        response.setAddress(university.getAddress());
        return response;
    }


}
