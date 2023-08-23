package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController()
@RequestMapping("/api/")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/cards")
    public List<CardDTO> getClients() {
        return cardRepository.findAll().stream()
                .map(CardDTO::new)
                .collect(toList());
    }

    @GetMapping("/cards/{code}")
    public CardDTO getClients(@PathVariable Long code) {
        return new CardDTO(cardRepository.findById(code).orElseThrow());
    }
}
