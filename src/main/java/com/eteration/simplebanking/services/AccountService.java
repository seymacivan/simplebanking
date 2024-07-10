package com.eteration.simplebanking.services;


import com.eteration.simplebanking.dto.AccountDto;
import com.eteration.simplebanking.exception.AccountNotFoundException;
import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.Transaction;
import com.eteration.simplebanking.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AccountService(AccountRepository accountRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    public AccountDto createBankAccount(AccountDto accountDTO) {
        Account account = modelMapper.map(accountDTO, Account.class);
        Account savedAccount = accountRepository.save(account);
        return modelMapper.map(savedAccount, AccountDto.class);
    }

    public Account findAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new AccountNotFoundException("Account with account number " + accountNumber + " not found.")
        );
        return account;
    }



    public String createTransaction(Account account, Transaction transaction) throws AccountNotFoundException, InsufficientBalanceException {
        transaction.setAccount(account);
        transaction.apply(account);
        account.getTransactions().add(transaction);
        accountRepository.save(account);
        return transaction.getApprovalCode();
    }
}
