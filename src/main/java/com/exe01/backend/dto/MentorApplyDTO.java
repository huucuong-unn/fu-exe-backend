package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorApplyDTO implements Serializable {

    private String feedback;

    private ApplicationDTO application;

    private CampaignDTO campaign;

    private MenteeDTO mentee;

    private String status;

}
