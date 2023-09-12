package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.utils.AccountUtils;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/cards")
    public ResponseEntity<Object> getAllCards(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null) {
            if (client.isAdmin()) {
                List<CardDTO> cards = cardService.findAll();
                return new ResponseEntity<>(cards, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/cards/{code}")
    public ResponseEntity<Object> getCardById(@PathVariable Long code, Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null) {
            Card card = cardService.findById(code);
            if (card == null) {
                return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
            } else {
                if (client.isAdmin()) {
                    CardDTO cardDTO = new CardDTO(card);
                    return new ResponseEntity<>(cardDTO, HttpStatus.ACCEPTED);
                }
                if (  card.isHolder(client)) {
                    CardDTO cardDTO = new CardDTO(card);
                    return new ResponseEntity<>(cardDTO, HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
                }
            }
        }
        return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("clients/current/cards")
    public ResponseEntity<Object> getCardsClient(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null) {
            if (client.hasCards()) {
                return new ResponseEntity<>(new ClientDTO(client).getCards(), HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Customer without cards", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("User Account does not exists", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "clients/current/cards")
    public ResponseEntity<String> createCard(@RequestParam CardType cardType,
                                             @RequestParam CardColor cardColor,
                                             Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null) {
            if (client.canCard(cardType, cardColor)) {
                String number = CardUtils.newNumberCard();
                while (cardService.findByNumber(number) != null) {
                    number =  AccountUtils.newNumberAccount() ;
                }
                Card card = new Card(cardType, cardColor, number);
                client.addCard(card);
                cardService.save(card);
                return new ResponseEntity<>("Card created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Cannot create that type of card", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("User account does not exists", HttpStatus.NOT_FOUND);
        }
    }

}
