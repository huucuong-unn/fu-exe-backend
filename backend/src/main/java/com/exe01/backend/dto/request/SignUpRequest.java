package com.exe01.backend.dto.request;


import com.exe01.backend.validation.ValidEmail;
import com.exe01.backend.validation.ValidPhone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotBlank(message = "Must not be blank")
    @NotNull(message = "Must not be null")
    private String firstName;

    @NotBlank(message = "Must not be blank")
    @NotNull(message = "Must not be null")
    private String lastName;

    @ValidEmail
    @NotBlank(message = "Must not be blank")
    @NotNull(message = "Must not be null")
    private String email;

    @NotBlank(message = "Must not be blank")
    @NotNull(message = "Must not be null")
    private String password;

    @ValidPhone
    @NotBlank(message = "Must not be blank")
    @NotNull(message = "Must not be null")
    private String phone;
}
