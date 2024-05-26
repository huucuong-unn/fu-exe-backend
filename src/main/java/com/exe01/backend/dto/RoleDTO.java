package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDTO extends BaseDTO implements Serializable {

    private String name;

    private String description;

    private String status;

}
