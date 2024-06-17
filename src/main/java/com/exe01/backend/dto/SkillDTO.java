package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class SkillDTO extends BaseDTO implements Serializable {

    private String name;

    private MajorDTO major;

    private String status;


    public SkillDTO(String name, MajorDTO major, String status) {
        this.name = name;
        this.major = major;
        this.status = status;
    }

}
