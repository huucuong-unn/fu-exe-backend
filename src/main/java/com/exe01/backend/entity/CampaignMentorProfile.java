package com.exe01.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "campaign_mentor_profile_tbl")
public class CampaignMentorProfile extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "campaign_id", referencedColumnName = "id")
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "mentor_profile_id", referencedColumnName = "id")
    private MentorProfile mentorProfile;

}
