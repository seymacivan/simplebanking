package com.eteration.simplebanking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.PhoneBillPaymentTransaction;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.repository.AccountRepository;
import com.eteration.simplebanking.services.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testDepositAndWithdrawalTransactions() throws Exception {
        Account account = new Account("Kerem Karaca", "17892");
        account.setBalance(1000.0);

        doReturn(Optional.of(account)).when(accountRepository).findByAccountNumber("17892");

        DepositTransaction depositTransaction = new DepositTransaction(500.0);
        accountService.createTransaction(account, depositTransaction);
        assertEquals(1500.0, account.getBalance(), 0.001);

        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(200.0);
        accountService.createTransaction(account, withdrawalTransaction);
        assertEquals(1300.0, account.getBalance(), 0.001);
    }

    @Test
    public void testDepositTransaction() throws Exception {
        Account account = new Account("Kerem Karaca", "17892");

        doReturn(Optional.of(account)).when(accountRepository).findByAccountNumber("17892");
        DepositTransaction depositTransaction = new DepositTransaction(1000.0);

        accountService.createTransaction(account, depositTransaction);
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdrawalTransaction() throws Exception {
        Account account = new Account("Kerem Karaca", "17892");
        account.setBalance(1000.0);

        doReturn(Optional.of(account)).when(accountRepository).findByAccountNumber("17892");
        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(50.0);

        accountService.createTransaction(account, withdrawalTransaction);
        assertEquals(950.0, account.getBalance(), 0.001);
    }

    @Test
    public void testPhoneBillPaymentTransaction() throws Exception {
        Account account = new Account("Kerem Karaca", "17892");
        account.setBalance(1000.0);

        doReturn(Optional.of(account)).when(accountRepository).findByAccountNumber("17892");
        PhoneBillPaymentTransaction phoneBillPaymentTransaction = new PhoneBillPaymentTransaction(
                96.50,
                "Vodafone",
                "5423345566"
        );

        accountService.createTransaction(account, phoneBillPaymentTransaction);
        assertEquals(903.5, account.getBalance(), 0.001);
    }

    @Test
    public void testInsufficientBalanceException() throws InsufficientBalanceException {
        Account account = new Account("Kerem Karaca", "17892");
        account.setBalance(100.0);

        doReturn(Optional.of(account)).when(accountRepository).findByAccountNumber("17892");
        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(150.0);

        assertThrows(InsufficientBalanceException.class, () -> {
            withdrawalTransaction.apply(account);
        });
    }
}
