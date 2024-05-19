package com.exe01.backend.dto.request;


import com.exe01.backend.validation.ValidEmail;
import com.exe01.backend.validation.ValidPhone;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank(message = "Must not be blank")
    private String firstName;
    @NotBlank(message = "Must not be blank")
    private String lastName;
    @ValidEmail
    private String email;
    @NotBlank
    private String password;
    @ValidPhone
    private String phone;
}
