package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client holder;
    private String number;
    private LocalDateTime date;
    private float balance;
    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    Set<Transaction> transaction = new HashSet<>();

    public Account() {
    }

    public Account(String number, LocalDateTime creationDate, float balance) {
        this.number = number;
        this.date = creationDate;
        this.balance = balance;
    }

    public Account(String number, float balance) {
        this.number = number;
        this.date = LocalDateTime.now();
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }


    public float getBalance() {
        return balance;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Client getHolder() {
        return this.holder;
    }



    public void setHolder(Client holder) {
        this.holder = holder;
    }

    public Set<Transaction> getTransaction() {
        return transaction;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction.getType().equals(TransactionType.CREDIT)) {
            setBalance(getBalance() + transaction.getAmount());
        } else {
            setBalance(getBalance() - transaction.getAmount());
        }
        transaction.setAccount(this);
        this.transaction.add(transaction);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = !this.deleted;
    }

    public boolean isValidClient(String email) {

        return this.holder.getEmail().equals(email);
    }
}
