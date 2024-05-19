package com.exe01.backend.entity;

import jakarta.persistence.*;
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

    @Column(name = "feedback")
    private String feedback;

    @OneToOne
    @JoinColumn(name = "application_id")
    private Application application;

}
