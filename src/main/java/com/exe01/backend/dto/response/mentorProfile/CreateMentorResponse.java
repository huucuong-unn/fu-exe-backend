package com.exe01.backend.dto.response.mentorProfile;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMentorResponse extends BaseDTO implements Serializable {

    private UUID id;

    private AccountDTO account;

    private String fullName;

    private String status;
}
