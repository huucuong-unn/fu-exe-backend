package com.exe01.backend.dto.request.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseCompanyRequest {

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    private String name;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 100, message = "Country must be less than or equal to 100 characters")
    private String country;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private String address;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private UUID accountId;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private MultipartFile avatarUrl;

    private String companyWebsiteUrl;

    private String facebookUrl;

    private String avatarUrlString;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private String description;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    private String workingTime;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private Integer companySize;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    private String companyType;


}
