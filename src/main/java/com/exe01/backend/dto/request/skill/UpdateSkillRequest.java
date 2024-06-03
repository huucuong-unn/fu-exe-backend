package com.exe01.backend.dto.request.skill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSkillRequest extends BaseSkillRequest {

    private String status;

}
