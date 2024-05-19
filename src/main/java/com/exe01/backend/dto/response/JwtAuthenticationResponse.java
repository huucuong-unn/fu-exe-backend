package com.exe01.backend.dto.response;

import com.exe01.backend.dto.UserDTO;
import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private UserDTO userDTO;
    private String token;
    private String refreshToken;
}
