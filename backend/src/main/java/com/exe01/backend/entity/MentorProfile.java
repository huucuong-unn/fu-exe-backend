package com.exe01.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mentor_profile_tbl")
public class MentorProfile extends BaseEntity {

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "requirement")
    private String requirement;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "description")
    private String description;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "short_description")
    private String shortDescription;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "profile_picture")
    private String profilePicture;

    @OneToMany(mappedBy = "mentorProfile")
    private List<SkillMentorProfile> skillMentorProfiles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "mentor_id", referencedColumnName = "id")
    private Mentor mentor;
}
