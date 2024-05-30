package com.exe01.backend.dto.request.campaign;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseCampaignRequest {

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private String name;
    @NotNull(message = "This field must not be null")
    private Date startDate;
    @NotNull(message = "This field must not be null")
    private Date endDate;
    @NotNull(message = "This field must not be null")
    private Date companyApplyStartDate;
    @NotNull(message = "This field must not be null")
    private Date companyApplyEndDate;
    @NotNull(message = "This field must not be null")
    private Date menteeApplyStartDate;
    @NotNull(message = "This field must not be null")
    private Date menteeApplyEndDate;
    @NotNull(message = "This field must not be null")
    private Date trainingStartDate;
    @NotNull(message = "This field must not be null")
    private Date trainingEndDate;

}
