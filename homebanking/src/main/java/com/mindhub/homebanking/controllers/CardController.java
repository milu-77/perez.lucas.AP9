package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
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
    public ResponseEntity<Object> getAllCards(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client != null) {
            if (client.isAdmin()) {
                List<CardDTO> cards = cardRepository.findAll().stream()
                        .map(CardDTO::new)
                        .collect(toList());
                return new ResponseEntity<>(cards, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/cards/{code}")
    public ResponseEntity<Object> getCardById(@PathVariable Long code, Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client != null) {
            Card card = cardRepository.findById(code).orElse(null);
            if (card == null) {
                return new ResponseEntity<>("Recurso No encontrado", HttpStatus.NOT_FOUND);
            } else {
                if (client.isAdmin()) {
                    CardDTO cardDTO = new CardDTO(card);
                    return new ResponseEntity<>(cardDTO, HttpStatus.ACCEPTED);
                }
                if (client.getEmail().equals(  card.getCardHolder().getEmail())) {
                    CardDTO cardDTO = new CardDTO(card);
                    return new ResponseEntity<>(cardDTO, HttpStatus.ACCEPTED);
                }
                else {
                    return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.UNAUTHORIZED);
                }
            }
        }
        return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("clients/current/cards")
    public ResponseEntity<Object> getCardsClient(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client != null) {
            if (client.hasCards()) {
                return new ResponseEntity<>(new ClientDTO(client).getCards(), HttpStatus.ACCEPTED);
            }

            else {
                return new ResponseEntity<>("Cliente Sin tarjetas", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Cliente inexistente", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<String> createCard(@RequestParam CardType cardType,
                                             @RequestParam CardColor cardColor,
                                             Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client != null) {
                if(client.canCard(cardType,cardColor)){
                    String number = Card.newNumberCard();
                    while (cardRepository.findByNumber(number) != null){
                        number = String.valueOf(Account.newNumberAccount());
                    }
                    Card card = new Card(cardType, cardColor, number);
                    client.addCard(card);
                    cardRepository.save(card);
                    return new ResponseEntity<>("se pudo Crear ! Tarjetas: " +
                            client.numCard(cardType) +
                            " el cliente es " +
                            client.getEmail(), HttpStatus.CREATED);
                }
                else {
                    return new ResponseEntity<>("No se puede Crear ese tipo de tarjeta", HttpStatus.FORBIDDEN);
                }
        } else {
            return new ResponseEntity<>("Cliente inexistente", HttpStatus.NOT_FOUND);
        }
    }

}
