package com.exe01.backend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;

    @NotBlank(message = "Must not be blank")
    private String firstName;

    @NotBlank(message = "Must not be blank")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;

    private String password;

    @NotBlank(message = "Phone must not be blank")
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number must start with 0 and have 10 digits in total")
    private String phone;

    @NotNull(message = "Date of birth must not be blank")
    private Long dob;

    private Boolean status;

    @NotNull(message = "Gender cannot be null")
    private boolean gender;

    private String avatarUrl;

    private List<Long> classIds;

    @NotNull(message = "User Role Id must not be null")
    private Long userRoleId;
    private String createBy;
    private String modifiedBy;
}
