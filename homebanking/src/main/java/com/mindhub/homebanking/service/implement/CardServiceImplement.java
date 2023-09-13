package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepository;

    @Override
    public List<CardDTO> findAll() {

        return cardRepository.findAll()
                .stream()
                .map(CardDTO::new)
                .collect(toList());
    }

    @Override
    public Card findById(Long code) {
        return cardRepository.findById(code).orElse(null);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }
}
