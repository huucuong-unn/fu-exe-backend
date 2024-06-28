package com.exe01.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "student_tbl")
public class Student extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id")
    private University university;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "This field must not be null")
    @Column(name = "dob")
    private Date dob;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 20, message = "Student code must be less than or equal to 20 characters")
    @Column(name = "student_code", nullable = false, unique = true)
    private String studentCode;

    private String frontStudentCard;

    private String backStudentCard;

    @OneToMany(mappedBy = "student")
    private List<Application> applications = new ArrayList<>();
}
