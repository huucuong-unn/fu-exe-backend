package com.exe01.backend.dto.request.mentee;


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
public class MenteeRequest {

    @NotNull(message = "This studentId must not be null")
    @NotBlank(message = "This studentId must not be blank")
    private UUID studentId;

}
