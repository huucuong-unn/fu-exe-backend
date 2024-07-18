package com.exe01.backend.Vnpay;

import com.exe01.backend.dto.request.transaction.BaseTransactionRequest;
import com.exe01.backend.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(value = "/api/v1")
public class VnPayController {
    PayService payService;

    public VnPayController(PayService payService) {
        this.payService = payService;
    }

    @PostMapping("/vnpay/payment")
    public String pay(@RequestBody BaseTransactionRequest transactionDTO, HttpServletRequest request) throws BaseException {
        try {
            return payService.payWithVNPAY(transactionDTO, request);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/vnpay/payment_infor")
    public RedirectView transaction(
            @RequestParam(value = "vnp_Amount") Double amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_ResponseCode") String responseCode,
            @RequestParam(value = "vnp_TxnRef") String txnRef
    ) throws BaseException {
        return payService.handleTransactionForPoint(amount, bankCode, responseCode, txnRef);

    }



}
