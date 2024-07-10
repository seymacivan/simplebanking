package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;
import lombok.NoArgsConstructor;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@NoArgsConstructor
@Entity
@DiscriminatorValue("PHONE_BILL_PAYMENT")
public class PhoneBillPaymentTransaction extends Transaction {
    private String operator;
    private String phoneNumber;

    public PhoneBillPaymentTransaction(double amount, String operator, String phoneNumber) {
        super(amount);
        this.operator = operator;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void apply(Account account) throws InsufficientBalanceException {
        account.withdraw(this.amount);
    }
}
