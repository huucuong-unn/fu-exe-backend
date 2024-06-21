package com.exe01.backend.dto.request.application;

import com.exe01.backend.validation.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseApplicationRequest {

    @NotNull(message = "Mentor ID must not be null")
    private UUID mentorId;

    @NotBlank(message = "Full name must not be blank")
    @NotNull(message = "Full name must not be null")
    private String fullName;

    @NotBlank(message = "User address must not be blank")
    @NotNull(message = "User address must not be null")
    private String userAddress;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email is Incorrect format")
    private String email;

    @NotBlank(message = "Phone number must not be blank")
    @ValidPhone
    private String phoneNumber;

    @NotBlank(message = "Facebook URL must not be blank")
    @NotNull(message = "Facebook URL must not be null")
    private String facebookUrl;

    @NotBlank(message = "Zalo account must not be blank")
    @NotNull(message = "Zalo account must not be null")
    private String zaloAccount;

    @NotBlank(message = "Reason for applying must not be blank")
    @NotNull(message = "Reason for applying must not be null")
    private String reasonApply;

    @NotBlank(message = "Introduction must not be blank")
    @NotNull(message = "Introduction must not be null")
    private String introduce;

    @NotBlank(message = "CV file must not be blank")
    @NotNull(message = "CV file must not be null")
    private MultipartFile cvFile;

    @NotBlank(message = "Student ID must not be blank")
    @NotNull(message = "Student ID must not be null")
    private UUID studentId;

}
