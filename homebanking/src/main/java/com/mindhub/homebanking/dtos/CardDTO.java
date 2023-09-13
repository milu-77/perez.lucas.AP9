package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.sql.Date;
import java.time.LocalDateTime;

public class CardDTO {
    private Long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private String cvv;
    private LocalDateTime truDate;
    private boolean deleted;

    public CardDTO() {
    }

    public CardDTO(Card card) {
        this.cardHolder = card.getCardHolder().getFirstName() +" "+ card.getCardHolder().getLastName();
        this.id = card.getId();
        this.type = card.getCardType();
        this.color = card.getCardColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.truDate =  card.getTruDate() ;
        this.deleted= card.isDeleted();
     }

    public Long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDateTime getThruDate() {
        return truDate;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
