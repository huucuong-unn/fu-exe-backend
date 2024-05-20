package com.exe01.backend.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MajorDTO extends BaseDTO{

    private UUID id;

    private String name;

    private String description;

    private String status;

}
