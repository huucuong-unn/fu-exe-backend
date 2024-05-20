package com.exe01.backend.dto;

import com.exe01.backend.entity.Mentor;
import com.exe01.backend.entity.SkillMentorProfile;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorProfiledDTO extends BaseDTO{

    private String linkedinUrl;

    private String requirement;

    private String description;

    private String shortDescription;

    private String profilePicture;

    private String status;

}
