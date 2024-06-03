package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillDTO extends BaseDTO implements Serializable {

    private String name;

    private MajorDTO major;

    private String status;

}
