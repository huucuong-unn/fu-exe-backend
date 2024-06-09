package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniversityDTO extends BaseDTO implements Serializable {

    private String name;

    private String address;

    private String status;

}
