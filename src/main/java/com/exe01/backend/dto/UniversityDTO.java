package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityDTO extends BaseDTO implements Serializable {

    private String name;

    private String address;

    private String status;

}
