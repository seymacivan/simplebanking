package com.eteration.simplebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDto {
    private String owner;
    private String accountNumber;
    private double balance;
    private List<TransactionDto> transactions;

}
