package com.exe01.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String id;

    private String userName;

    private String password;

    private String avatarUrl;

    private Boolean status;

    private Date createdDate;

    private Date modifiedDate;

    private String createBy;

    private String modifiedBy;
}
