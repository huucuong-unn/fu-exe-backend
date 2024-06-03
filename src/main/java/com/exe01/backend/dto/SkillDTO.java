package com.exe01.backend.dto;

import com.exe01.backend.entity.Major;
import com.exe01.backend.entity.SkillMentorProfile;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillDTO extends BaseDTO implements Serializable {

    private String name;

    private MajorDTO major;

    private String status;

}
