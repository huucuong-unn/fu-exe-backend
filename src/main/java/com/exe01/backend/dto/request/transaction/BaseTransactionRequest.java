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

    private Double amount;

    private String status;

    private Integer points;

    private UUID accountId;
    public String vnp_OrderInfo = "Point";
    public String vnp_OrderType = "200000";
    public String vnp_TxnRef;


}
