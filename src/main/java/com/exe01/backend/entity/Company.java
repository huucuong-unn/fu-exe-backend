package com.exe01.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "company_tbl")
public class Company extends BaseEntity {


    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "avatar_url")
    private String avatarUrl;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 100, message = "Company type must be less than or equal to 100 characters")
    @Column(name = "company_type", nullable = false)
    private String companyType;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "company_size")
    private Integer companySize;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 100, message = "Country must be less than or equal to 100 characters")
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "working_time")
    private String workingTime;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "overtime_policy")
    private String overtimePolicy;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "company_website_url")
    private String company_website_url;

    @Column(name = "facebook_url")
    private String facebook_url;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "company")
    private List<Mentor> mentors = new ArrayList<>();

}
