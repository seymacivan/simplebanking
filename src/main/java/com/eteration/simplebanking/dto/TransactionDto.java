package com.eteration.simplebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDto {
    private LocalDateTime date;
    private double amount;
    private String type;
    private String approvalCode;
}
