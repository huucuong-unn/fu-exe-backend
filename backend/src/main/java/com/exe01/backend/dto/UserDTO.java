package com.exe01.backend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private Date dob;

    private Boolean gender;

    private String avatarUrl;

    private Long userRoleId;

    private String createBy;

    private String modifiedBy;
}
