package com.exe01.backend.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserRequest {

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    @Size(max = 255, message = "First name must be smaller than 255")
    private String firstName;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    @Size(max = 255, message = "Last name must be smaller than 255 character")
    private String lastName;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    @Size(max = 255, message = "Email must be smaller than 255 character")
    private String email;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    @Size(max = 255, message = "Password must be smaller than 255 character")
    private String password;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    @Size(max = 255, message = "Phone must be smaller than 255 character")
    private String phone;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    private Date dob;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    private Boolean gender;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    @Size(max = 255, message = "Avatar url must be smaller than 255 character")
    private String avatarUrl;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    private Long roleId;
}
