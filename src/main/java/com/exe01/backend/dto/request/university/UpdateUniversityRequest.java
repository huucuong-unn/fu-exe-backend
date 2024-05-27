package com.exe01.backend.dto.request.university;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUniversityRequest extends BaseUniversityRequest {

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private String status;

}
