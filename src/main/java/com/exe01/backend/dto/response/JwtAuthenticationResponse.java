package com.exe01.backend.dto.response;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {

    private UUID id;

    private String username;

    private UUID studentId;

    private UUID mentorId;

    private UUID companyId;

    private String avatarUrl;

    private String status;

    private String email;

    private String role;

    private Integer point;

    private String token;

    private String refreshToken;

}
