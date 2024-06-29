package com.exe01.backend.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoginRequest {

    @NotBlank(message = "username must not be blank")
    @NotNull(message = "username must not be null")
    private String emailOrUsername;

    @NotBlank(message = "password must not be blank")
    @NotNull(message = "password must not be null")
    private String password;

    private String loginWithRole;

}
