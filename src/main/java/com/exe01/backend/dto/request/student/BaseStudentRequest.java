package com.exe01.backend.dto.request.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseStudentRequest {

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private UUID universityId;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private UUID accountId;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    private String name;

    @NotNull(message = "This field must not be null")
    private Date dob;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 20, message = "Student code must be less than or equal to 20 characters")
    private String studentCode;

}
