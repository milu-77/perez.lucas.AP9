package com.mindhub.homebanking.models;

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
        this.number = Card.generateNumber();
        this.cvv = Card.generateCvn();
        this.truDate = Card.generateTruDate(fromDate, 5);
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

    public Card(CardType cardType, CardColor cardColor, LocalDateTime fromDate) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = Card.generateNumber();
        this.cvv = Card.generateCvn();
        this.truDate = Card.generateTruDate(fromDate, 5);
        this.fromDate = fromDate;
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

    private static String generateCvn() {
        StringBuilder cvn = new StringBuilder();
        Random random = new Random();
        for (int a = 0; a < 3; a++) {
            int randomNumber = random.nextInt(10); // Genera números entre 0 (inclusive) y 1000 (exclusive)
            cvn.append(randomNumber);
        }


        return cvn.toString();
    }

    private static String generateNumber() {
        StringBuilder number = new StringBuilder();
        Random random = new Random();
        for (int a = 0; a < 16; a++) {
            int randomNumber = random.nextInt(10); // Genera números entre 0 (inclusive) y 1000 (exclusive)
            if (a % 4 == 0 && a != 0) {
                number.append("-");
            }
            number.append(randomNumber);
        }

        return number.toString();
    }

    private static LocalDateTime generateTruDate(LocalDateTime fromDate, int years) {
        return fromDate.plusYears(years);
    }
}
