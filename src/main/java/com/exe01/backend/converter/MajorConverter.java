package com.exe01.backend.converter;

import com.exe01.backend.dto.MajorDTO;
import com.exe01.backend.entity.Major;

public class MajorConverter {

    public static MajorDTO toDto(Major major) {
        MajorDTO majorDTO = new MajorDTO();
        majorDTO.setId(major.getId());
        majorDTO.setName(major.getName());
        majorDTO.setDescription(major.getDescription());
        majorDTO.setStatus(major.getStatus());
        majorDTO.setCreatedDate(major.getCreatedDate());
        majorDTO.setModifiedDate(major.getModifiedDate());
        majorDTO.setCreatedBy(major.getCreatedBy());
        majorDTO.setModifiedBy(major.getModifiedBy());

        return majorDTO;
    }

    public static Major toEntity(MajorDTO majorDTO) {
        Major major = new Major();
        major.setId(majorDTO.getId());
        major.setName(majorDTO.getName());
        major.setDescription(majorDTO.getDescription());
        major.setStatus(majorDTO.getStatus());
        major.setCreatedDate(majorDTO.getCreatedDate());
        major.setModifiedDate(majorDTO.getModifiedDate());
        major.setCreatedBy(majorDTO.getCreatedBy());
        major.setModifiedBy(majorDTO.getModifiedBy());

        return major;
    }
}
