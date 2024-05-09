package com.exe01.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable  = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

//    @CreationTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "create_date")
//    private Date createDate;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "modified_by")
    private String modifiedBy;


    @Column(name = "modified_date")
    private Date modifiedDate;

    @PrePersist
    protected void onCreate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            createBy = "Anonymous";
            modifiedBy = "Anonymous";
        }
        else {
            createBy = authentication.getName();
            modifiedBy = authentication.getName();
        }

        createdDate = new Date();
        modifiedDate =  new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        modifiedBy = authentication.getName();
        modifiedDate = new Date();
    }


    public void markModified() {
        this.onUpdate();
    }
}
