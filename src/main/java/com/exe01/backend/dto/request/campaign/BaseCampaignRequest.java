package com.exe01.backend.dto.request.campaign;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseCampaignRequest {

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private String name;
    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private MultipartFile img;
    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date companyApplyStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date companyApplyEndDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date menteeApplyStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date menteeApplyEndDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date trainingStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date trainingEndDate;

}
