package com.mindhub.homebanking.models;

import com.mindhub.homebanking.utils.CardUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private String cvv;
    private LocalDateTime truDate;
    private LocalDateTime fromDate;
    private boolean deleted = Boolean.FALSE;

    public Card() {
    }

    public Card(Client cardholder, CardType cardType, CardColor cardColor, String number, String cvn, LocalDateTime truDate, LocalDateTime fromDate) {
        this.cardHolder = cardholder;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvn;
        this.truDate = truDate;
        this.fromDate = fromDate;
    }

    public Card(Client cardholder, CardType cardType, CardColor cardColor, LocalDateTime fromDate) {
        this.cardHolder = cardholder;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = CardUtils.newNumberCard();
        this.cvv = CardUtils.generateCvn();
        this.truDate = CardUtils.generateTruDate(fromDate, 5);
        this.fromDate = fromDate;
    }

    public Card(CardType cardType, CardColor cardColor, String number, String cvv, LocalDateTime truDate, LocalDateTime fromDate) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.truDate = truDate;
        this.fromDate = fromDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted =  !this.deleted;
    }

    public Card(CardType cardType, CardColor cardColor, LocalDateTime fromDate) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = CardUtils.newNumberCard();
        this.cvv = CardUtils.generateCvn();
        this.truDate = CardUtils.generateTruDate(fromDate, 5);
        this.fromDate = fromDate;
    }

    public Card(CardType cardType, CardColor cardColor) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = CardUtils.newNumberCard();
        this.cvv = CardUtils.generateCvn();
        this.truDate = CardUtils.generateTruDate(LocalDateTime.now(), 5);
        this.fromDate = LocalDateTime.now();
    }

    public Card(CardType cardType, CardColor cardColor, String number) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = CardUtils.generateCvn();
        this.truDate = CardUtils.generateTruDate(LocalDateTime.now(), 5);
        this.fromDate = LocalDateTime.now();
    }


    public Client getCardHolder() {
        return cardHolder;
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDateTime getTruDate() {
        return truDate;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public Long getId() {
        return id;
    }

    public void setCardHolder(Client cardholder) {
        this.cardHolder = cardholder;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setTruDate(LocalDateTime truDate) {
        this.truDate = truDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public void setClient(Client client) {
        this.cardHolder = client;
    }


    public boolean isHolder(Client client) {
        return  this.getCardHolder().getEmail().equals(client.getEmail());

    }
}


