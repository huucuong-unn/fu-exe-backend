package com.exe01.backend.dto.response.mentorProfile;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindMentorProfileByIdResponse extends BaseDTO implements Serializable {

    private AccountDTO account;

    private String status;

}
