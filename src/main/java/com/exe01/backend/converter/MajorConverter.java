package com.exe01.backend.converter;

import com.exe01.backend.dto.MajorDTO;
import com.exe01.backend.entity.Major;

public class MajorConverter {

    public static MajorDTO toDTO(Major major) {
        MajorDTO majorDTO = new MajorDTO();
        majorDTO.setId(major.getId());
        majorDTO.setName(major.getName());
        majorDTO.setDescription(major.getDescription());
        majorDTO.setStatus(major.getStatus());
        majorDTO.setCreatedDate(major.getCreatedDate());
        majorDTO.setModifiedDate(major.getModifiedDate());
        majorDTO.setCreateBy(major.getCreateBy());
        majorDTO.setModifiedBy(major.getModifiedBy());

        return majorDTO;
    }

    public static Major toEntity(MajorDTO majorDTO) {
        Major major = new Major();
        major.setId(major.getId());
        major.setName(major.getName());
        major.setDescription(major.getDescription());
        major.setStatus(major.getStatus());

        return major;
    }
}
