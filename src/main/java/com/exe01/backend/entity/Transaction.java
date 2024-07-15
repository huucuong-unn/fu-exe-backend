package com.exe01.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction_tbl")
public class Transaction extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @NotNull(message = "This field must not be null")
    @Column(name = "amount")
    private Double amount;

    @NotNull(message = "This field must not be null")
    @Column(name = "points")
    private Integer points;

    private  String type;

}
