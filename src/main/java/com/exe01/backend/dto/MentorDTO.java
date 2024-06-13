package com.exe01.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorDTO extends BaseDTO implements Serializable {

    private CompanyDTO company;

    private AccountDTO account;

    private String status;

}
