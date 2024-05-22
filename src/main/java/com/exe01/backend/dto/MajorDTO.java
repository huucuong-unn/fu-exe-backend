package com.exe01.backend.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MajorDTO extends BaseDTO implements Serializable {

    private UUID id;

    private String name;

    private String description;

    private String status;

}
