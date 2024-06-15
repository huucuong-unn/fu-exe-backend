package com.exe01.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MajorDTO extends BaseDTO implements Serializable {

    private UUID id;

    private String name;

    private String description;

    private String status;

    public MajorDTO(UUID id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

}
