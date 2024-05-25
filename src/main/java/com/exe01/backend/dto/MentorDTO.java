package com.exe01.backend.dto;

import com.exe01.backend.entity.MentorProfile;
import lombok.*;

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
