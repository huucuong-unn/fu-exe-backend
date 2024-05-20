package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDTO {

    private Date createdDate;

    private Date modifiedDate;

    private String createBy;

    private String modifiedBy;
}
