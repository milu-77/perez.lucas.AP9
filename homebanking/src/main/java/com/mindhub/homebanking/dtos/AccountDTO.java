package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class AccountDTO {
    private long id;
    private String number;

    private LocalDateTime date;
    private float balance;
    Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.date = account.getDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransaction().stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toSet());


    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public float getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}
