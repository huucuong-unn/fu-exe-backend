package com.exe01.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    @NotBlank(message = "Must not be blank")
    @NotNull(message = "Must not be null")
    private String email;

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    private String password;
}
