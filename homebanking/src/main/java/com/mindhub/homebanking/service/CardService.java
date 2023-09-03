package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface CardService {
    List<CardDTO> findAll();

    Card findById(Long code);

    Card findByNumber(String number);

    void save(Card card);
}
