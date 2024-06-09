package com.exe01.backend.dto.request.mentorApply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseMentorApplyRequest {

    @NotBlank(message = "This feedback must not be blank")
    @NotNull(message = "This feedback must not be null")
    private String feedback;

    @NotBlank(message = "This applicationId must not be blank")
    @NotNull(message = "This applicationId must not be null")
    private UUID applicationId;

    @NotBlank(message = "This menteeId must not be blank")
    @NotNull(message = "This menteeId must not be null")
    private UUID menteeId;

}
