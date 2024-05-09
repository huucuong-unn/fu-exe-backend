package com.exe01.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_tbl")
public class User extends BaseEntity implements UserDetails {
    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    @Column(name = "first_name", length = 255)
    private String firstName;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    @Column(name = "last_name", length = 255)
    private String lastName;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    @Column(name = "email", length = 255)
    private String email;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    @Column(name = "password", length = 255)
    private String password;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    @Column(name = "phone", length = 255)
    private String phone;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "gender")
    private Boolean gender;

    @NotNull(message = "This field must not be null !")
    @NotBlank(message = "This field must not be blank !")
    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @ManyToOne
    @JoinColumn(name = "role")
    private Role roleId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roleId.getName()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
