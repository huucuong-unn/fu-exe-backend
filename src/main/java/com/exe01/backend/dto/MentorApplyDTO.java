package com.exe01.backend.dto;

import com.exe01.backend.entity.Application;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorApplyDTO {

    private String feedback;
    private ApplicationDTO application;
    private MenteeDTO mentee;

}
