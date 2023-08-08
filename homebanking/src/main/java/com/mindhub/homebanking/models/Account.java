package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.time.LocalDateTime;


@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client holder ;
    private String number;
    private LocalDateTime  date;
   private float balance;
    public Account() {

    }

    public Account(   String number, LocalDateTime creationDate, float balance) {

        this.number = number;
        this.date = creationDate;
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
        return this.holder ;
    }

    public void setHolder(Client holder ) {
        this.holder  = holder ;
    }
}
