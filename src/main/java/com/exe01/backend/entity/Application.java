package com.exe01.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "application_tbl")
public class Application extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "mentor_id",referencedColumnName = "id")
    private Mentor mentor;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "zalo_account")
    private String zaloAccount;

    @Column(name = "reason_apply")
    private String reasonApply;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "cv_file")
    private String cvFile;

    @OneToOne(mappedBy = "application")
    private MentorApply mentorApply;

    @ManyToOne
    @JoinColumn(name = "student_id",referencedColumnName = "id")
    private Student student;


}
