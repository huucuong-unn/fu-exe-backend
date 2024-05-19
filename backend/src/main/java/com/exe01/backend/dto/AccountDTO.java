package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    private UUID id;

    private String username;

    private String password;

    private String avatarUrl;

    private Date createdDate;

    private String createBy;

    private String modifiedBy;

    private Date modifiedDate;

    private String status;

    private String email;

    private String role;

}
