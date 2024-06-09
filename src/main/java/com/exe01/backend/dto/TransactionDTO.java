package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO extends BaseDTO implements Serializable {

    private BigDecimal amount;

    private String status;

    private Integer points;

    private AccountDTO account;

}
