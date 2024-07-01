package com.exe01.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO implements Serializable {

    private UUID id;
    private String username;
    private String avatarUrl;
    private Date createdDate;
    private String createBy;
    private String modifiedBy;
    private Date modifiedDate;
    private String status;
    private String email;
    private RoleDTO role;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CompanyDTO company;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StudentDTO student;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MentorDTO mentor;
    private Integer point;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDTO that = (AccountDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(avatarUrl, that.avatarUrl) &&
                Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(createBy, that.createBy) &&
                Objects.equals(modifiedBy, that.modifiedBy) &&
                Objects.equals(modifiedDate, that.modifiedDate) &&
                Objects.equals(status, that.status) &&
                Objects.equals(email, that.email) &&
                Objects.equals(role, that.role) &&
                Objects.equals(company, that.company) &&
                Objects.equals(student, that.student) &&
                Objects.equals(mentor, that.mentor) &&
                Objects.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, avatarUrl, createdDate, createBy, modifiedBy, modifiedDate, status, email, role, company, student, mentor, point);
    }
}
