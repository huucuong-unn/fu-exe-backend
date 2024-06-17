package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDTO extends BaseDTO implements Serializable {

    private String name;

    private String img;

    private String description;

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
