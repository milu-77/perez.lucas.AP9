package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class AccountDTO {

    private long id;
    private String number;
    private float balance;
    private LocalDateTime creationDate;
    public String mailHolder;
    Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransaction().stream().map(TransactionDTO::new).collect(Collectors.toSet());
        mailHolder = account.getHolder().getEmail();
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public float getBalance() {
        return balance;
    }

    public LocalDateTime getDate() {
        return creationDate;
    }

    public String getMailHolder() {
        return mailHolder;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}