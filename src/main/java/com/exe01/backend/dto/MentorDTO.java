package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorDTO extends BaseDTO implements Serializable {

    private AccountDTO account;

    private List<MentorProfileDTO> mentorProfiles;

    private String status;

}
