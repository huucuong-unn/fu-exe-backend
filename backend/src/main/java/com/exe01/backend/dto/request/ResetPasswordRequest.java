package com.exe01.backend.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    String email;
    String newPassword;
}
