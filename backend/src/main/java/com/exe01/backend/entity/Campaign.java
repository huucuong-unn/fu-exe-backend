package com.exe01.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "campaign_tbl")
public class Campaign extends BaseEntity {

    @NotBlank(message = "This field must not be blank")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "company_apply_start_date")
    private Date companyApplyStartDate;

    @Column(name = "company_apply_end_date")
    private Date companyApplyEndDate;

    @Column(name = "mentee_apply_start_date")
    private Date menteeApplyStartDate;

    @Column(name = "mentee_apply_end_date")
    private Date menteeApplyEndDate;

    @Column(name = "training_start_date")
    private Date trainingStartDate;

    @Column(name = "training_end_date")
    private Date trainingEndDate;

    @Column(name = "number_of_session")
    private Integer numberOfSession;

    @Column(name = "min_offline_session")
    private Integer minOfflineSession;

    @Column(name = "min_online_session")
    private Integer minOnlineSession;

    @Column(name = "min_session_duration")
    private Integer minSessionDuration;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;
}
