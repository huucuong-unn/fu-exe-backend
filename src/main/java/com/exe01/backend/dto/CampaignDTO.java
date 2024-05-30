package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDTO extends BaseDTO{

    private String name;
    private Date startDate;
    private Date endDate;
    private Date companyApplyStartDate;
    private Date companyApplyEndDate;
    private Date menteeApplyStartDate;
    private Date menteeApplyEndDate;
    private Date trainingStartDate;
    private Date trainingEndDate;
    private String status;

}
