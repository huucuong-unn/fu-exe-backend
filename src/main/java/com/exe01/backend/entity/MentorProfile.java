package com.exe01.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "google_meet_url")
    private String googleMeetUrl;

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
    @Size(max = 200, message = "Short description must be less than or equal to 200 characters")
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

    @OneToMany(mappedBy = "mentorProfile")
    private List<CampaignMentorProfile> campaignMentorProfiles = new ArrayList<>();
}
