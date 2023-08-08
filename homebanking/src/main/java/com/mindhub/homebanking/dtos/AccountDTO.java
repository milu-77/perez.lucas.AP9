package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDateTime;

public class AccountDTO {
     private String number;
    private LocalDateTime date;
    private float balance;

    public AccountDTO(Account account) {
        this.number = account.getNumber();
        this.date = account.getDate();
        this.balance = account.getBalance();
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
}
