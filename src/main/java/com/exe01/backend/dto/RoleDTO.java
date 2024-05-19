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
public class RoleDTO {
    private UUID id;
    private String name;
    private String description;
    private Boolean status;
    private Date createdDate;
    private Date modifiedDate;
    private String createBy;
    private String modifiedBy;
}
