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
@Table(name = "mentor_tbl")
public class Mentor extends BaseEntity {

    @OneToMany(mappedBy = "mentor")
    private List<MentorProfile> mentorProfiles = new ArrayList<>();

    @OneToMany(mappedBy = "mentor")
    private List<Application> applications;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @Column(name = "full_name")
    @NotNull
    @NotBlank
    private String fullName;

}
