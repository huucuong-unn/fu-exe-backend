package com.exe01.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "skill_mentor_profile_tbl")
public class SkillMentorProfile extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "mentor_profile_id", referencedColumnName = "id")
    private MentorProfile mentorProfile;

    @ManyToOne
    @JoinColumn(name = "skill_id", referencedColumnName = "id")
    private Skill skill;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "skill_level")
    private String skillLevel;
}
