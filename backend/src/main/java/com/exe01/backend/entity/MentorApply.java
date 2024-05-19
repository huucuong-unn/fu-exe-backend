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
@Table(name = "mentor_apply_tbl")
public class MentorApply extends BaseEntity {

    @NotNull(message = "This field must not be null")
    @NotBlank(message = "This field must not be blank")
    @Column(name = "feedback")
    private String feedback;

    @NotNull(message = "This field must not be null")
    @OneToOne
    @JoinColumn(name = "application_id")
    private Application application;

}
