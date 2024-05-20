package com.exe01.backend.dto.request.mentorProfile;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseMentorProfileRequest {

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private String linkedinUrl;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private String requirement;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private String description;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 200, message = "Short description must be less than or equal to 200 characters")
    private String shortDescription;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private String profilePicture;
}
