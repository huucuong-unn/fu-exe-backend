package com.exe01.backend.dto.request.mentor;

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
public class BaseMentorRequest {

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private UUID accountId;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private UUID companyId;


}
