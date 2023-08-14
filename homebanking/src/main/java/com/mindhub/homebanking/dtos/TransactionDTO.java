package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private TransactionType type;
    private LocalDateTime date;
    private String description;
    private float amount;

    public TransactionDTO(Transaction transaction) {
        this.type = transaction.getType();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();
        this.amount = transaction.getAmount();
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public float getAmount() {
        return amount;
    }
}
