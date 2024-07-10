package com.eteration.simplebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhoneBillPaymentRequest {
    private String operator;
    private String phoneNumber;
    private double amount;
}