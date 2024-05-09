package com.exe01.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "role_tbl")
public class Role extends BaseEntity {
    @NotBlank(message = "Must not be blank")
    @NotNull(message = "Must not be null")
    @Size(max = 30, message = "Name must be less than or equal to 30 characters")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Must not be blank")
    @NotNull(message = "Must not be null")
    @Size(min = 10, max = 100, message = "Description must be between 10 and 100 characters")
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "roleId")
    private List<User> users = new ArrayList<>();

    @Column(name = "status")
    private Boolean status;
}
