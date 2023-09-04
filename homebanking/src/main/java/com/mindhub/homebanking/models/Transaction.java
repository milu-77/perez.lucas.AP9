package com.mindhub.homebanking.models;

import com.mindhub.homebanking.dtos.AccountDTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
    private TransactionType type;
    private LocalDateTime date;
    private String description;
    private float amount;

    public Transaction() {
    }

    public Transaction(TransactionType type, LocalDateTime date, String description, float amount) {
        this.type = type;
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    public Transaction(TransactionType transactionType, String description, float amount) {
        this.type = transactionType;
        this.date = LocalDateTime.now();
        this.description = description;
        this.amount = amount;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public AccountDTO getAccount() {
        return new AccountDTO(account);
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

    public boolean isHolder(Client client) {
        return this.getAccount().getMailHolder().equals(client.getEmail());
    }
}
