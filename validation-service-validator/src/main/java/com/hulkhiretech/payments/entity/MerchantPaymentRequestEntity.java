package com.hulkhiretech.payments.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MerchantPaymentRequestEntity {

    private Integer id;

    private String endUserID;

    private String merchantTxnReference;

    private String transactionRequest;

    private LocalDateTime creationDate;
}