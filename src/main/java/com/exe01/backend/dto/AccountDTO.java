package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class AccountDTO implements Serializable  {

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

    private Integer point;

    public AccountDTO(UUID id, String username, String avatarUrl, Date createdDate,
                      String createBy, String modifiedBy, Date modifiedDate,
                      String status, String email, RoleDTO role, Integer point) {
        this.id = id;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.createdDate = createdDate;
        this.createBy = createBy;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
        this.status = status;
        this.email = email;
        this.role = role;
        this.point = point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDTO that = (AccountDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(avatarUrl, that.avatarUrl) && Objects.equals(createdDate, that.createdDate) && Objects.equals(createBy, that.createBy) && Objects.equals(modifiedBy, that.modifiedBy) && Objects.equals(modifiedDate, that.modifiedDate) && Objects.equals(status, that.status) && Objects.equals(email, that.email) && Objects.equals(role, that.role) && Objects.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, avatarUrl, createdDate, createBy, modifiedBy, modifiedDate, status, email, role, point);
    }
}
