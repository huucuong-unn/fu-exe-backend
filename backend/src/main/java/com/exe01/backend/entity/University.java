package com.exe01.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "university_tbl")
public class University extends BaseEntity {

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "university")
    private List<Student> students = new ArrayList<>();
}
