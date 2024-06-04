package com.exe01.backend.dto.request.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseTransactionRequest {

    private UUID accountId;

    private BigDecimal amount;

    private Integer points;

}
