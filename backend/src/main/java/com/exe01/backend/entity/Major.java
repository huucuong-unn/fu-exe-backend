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
@Table(name = "major_tbl")
public class Major extends BaseEntity {

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 500, message = "Description must be less than or equal to 500 characters")
    @NotNull(message = "This field must not be null")
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "major")
    private List<Skill> skills = new ArrayList<>();
}
