package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorDTO extends BaseDTO implements Serializable {

    private UUID id;

    private AccountDTO account;

    private List<MentorProfileDTO> mentorProfiles;

    private String status;

}
