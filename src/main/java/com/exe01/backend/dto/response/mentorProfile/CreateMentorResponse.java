package com.exe01.backend.dto.response.mentorProfile;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.BaseDTO;
import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.dto.request.mentor.BaseMentorRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMentorResponse extends BaseDTO {

    private UUID id;

    private AccountDTO account;

    private String status;
}
