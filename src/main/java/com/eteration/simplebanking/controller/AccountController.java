package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.dto.AccountDto;
import com.eteration.simplebanking.dto.PhoneBillPaymentRequest;
import com.eteration.simplebanking.dto.TransactionStatus;
import com.eteration.simplebanking.exception.AccountNotFoundException;
import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.PhoneBillPaymentTransaction;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account/v1")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountDto> createBankAccount(@RequestBody AccountDto accountDTO) {
        AccountDto createdBankAccount = accountService.createBankAccount(accountDTO);
        return new ResponseEntity<>(createdBankAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        try {
            Account account = accountService.findAccount(accountNumber);
            return ResponseEntity.ok(account);
        } catch (AccountNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/credit/{accountNumber}")
    public ResponseEntity<TransactionStatus> credit(
            @PathVariable String accountNumber,
            @RequestBody DepositTransaction transactionRequest) {

        try {
            Account account = accountService.findAccount(accountNumber);
            DepositTransaction depositTransaction = new DepositTransaction(transactionRequest.getAmount());
            String approvalCode = accountService.createTransaction(account, depositTransaction);
            return ResponseEntity.ok(new TransactionStatus("OK", approvalCode));
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientBalanceException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/debit/{accountNumber}")
    public ResponseEntity<TransactionStatus> debit(
            @PathVariable String accountNumber,
            @RequestBody WithdrawalTransaction transactionRequest) {

        try {
            Account account = accountService.findAccount(accountNumber);
            WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(transactionRequest.getAmount());
            String approvalCode = accountService.createTransaction(account, withdrawalTransaction);
            return ResponseEntity.ok(new TransactionStatus("OK", approvalCode));
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientBalanceException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/phone-bill-payment/{accountNumber}")
    public ResponseEntity<TransactionStatus> phoneBillPayment(
            @PathVariable String accountNumber,
            @RequestBody PhoneBillPaymentRequest transactionRequest) {

        try {
            Account account = accountService.findAccount(accountNumber);
            PhoneBillPaymentTransaction transaction = new PhoneBillPaymentTransaction(
                    transactionRequest.getAmount(),
                    transactionRequest.getOperator(),
                    transactionRequest.getPhoneNumber());

            String approvalCode = accountService.createTransaction(account, transaction);
            return ResponseEntity.ok(new TransactionStatus("OK", approvalCode));
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientBalanceException e) {
            throw new RuntimeException(e);
        }
    }
}