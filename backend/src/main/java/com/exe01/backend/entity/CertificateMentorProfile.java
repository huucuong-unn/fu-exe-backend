package com.exe01.backend.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "certificate_mentor_profile")
public class CertificateMentorProfile extends BaseEntity {
    @NotNull(message = "This field must not be null")
    @OneToOne
    @JoinColumn(name = "certificate_id", referencedColumnName = "id")
    private Certificate certificate;

    @NotNull(message = "This field must not be null")
    @OneToOne
    @JoinColumn(name = "mentor_profile_id", referencedColumnName = "id")
    private MentorProfile mentorProfile;
}
