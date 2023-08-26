package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController()
@RequestMapping("/api/")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/cards")
    public ResponseEntity<Object> getCards(Authentication authentication) {
        if (authentication.getName().contains("admin@admin.com")) {
            List<CardDTO> cards = cardRepository.findAll().stream()
                    .map(CardDTO::new)
                    .collect(toList());
            return new ResponseEntity<>(cards, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.BAD_GATEWAY);
        }
    }


    @GetMapping("/cards/{code}")
    public ResponseEntity<Object>  getClients(@PathVariable Long code,Authentication authentication) {
        Card card = cardRepository.findById(code).orElse(null);
        if (card == null) {
            return new ResponseEntity<>("Recurso No encontrado", HttpStatus.BAD_GATEWAY);
        } else {
            if ( card.getCardHolder().getEmail().equals(authentication.getName()) || authentication.getName().contains("admin@admin.com")) {
                CardDTO cardDTO = new CardDTO(card);
                return new ResponseEntity<>(cardDTO, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.BAD_GATEWAY);
            }
        }
     }

    @GetMapping("clients/current/cards")
    public ResponseEntity<Object> getCard(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client != null) {
            if (client.hasCards()) {
                return new ResponseEntity<>(new ClientDTO(client).getCards(), HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Cliente Sin tarjetas", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Cliente inexistente", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<String> createCard(@RequestParam String cardType,
                                             @RequestParam String cardColor,
                                             Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client != null) {
            if (client.numCard(cardType) < 3) {
                String number = Card.newNumberCard();
                while (cardRepository.findByNumber(number) != null) number = String.valueOf(Account.newNumberAccount());
                Card card = new Card(cardType, cardColor, number);
                client.addCard(card);
                cardRepository.save(card);
                return new ResponseEntity<>("se puede Crear ! Tarjetas: " + client.numCard(cardType) + " el cliente es " + client.getEmail(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Cliente con tres Tarjetas de tipo: " + cardType + ", imposible agregar mas", HttpStatus.FORBIDDEN);
            }
        }
        else{
            return new ResponseEntity<>("Cliente inexistente", HttpStatus.FORBIDDEN);
        }
    }

}
