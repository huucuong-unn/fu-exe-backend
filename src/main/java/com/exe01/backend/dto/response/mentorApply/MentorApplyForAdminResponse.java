package com.exe01.backend.dto.response.mentorApply;

import com.exe01.backend.dto.MenteeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MentorApplyForAdminResponse {
private MenteeDTO mentee;
private String mentorFullName;
private String companyName;
private String campaignName;

private  String status;


}
