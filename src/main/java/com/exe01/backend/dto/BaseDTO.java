package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDTO {
    private UUID id;

    private Date createdDate;

    private Date modifiedDate;

    private String createdBy;

    private String modifiedBy;
}
