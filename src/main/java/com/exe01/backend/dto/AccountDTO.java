package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO implements Serializable {

    private UUID id;

    private String username;

    private String avatarUrl;

    private Date createdDate;

    private String createBy;

    private String modifiedBy;

    private Date modifiedDate;

    private String status;

    private String email;

    private RoleDTO role;

    private Integer point;

}
