package com.exe01.backend.Vnpay;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TransactionVNPDTO implements Serializable {
    private String status;
    private String message;
    private String data;
}
