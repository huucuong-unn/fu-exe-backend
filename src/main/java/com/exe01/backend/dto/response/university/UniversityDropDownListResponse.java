package com.exe01.backend.dto.response.university;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniversityDropDownListResponse implements Serializable {
    UUID id;
    String name;
    String address;
}
