package com.exe01.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Must not be blank")
    @NotNull(message = "Must not be null")
    String email;

    @NotBlank(message = "Must not be blank")
    @NotNull(message = "Must not be null")
    String newPassword;
}
