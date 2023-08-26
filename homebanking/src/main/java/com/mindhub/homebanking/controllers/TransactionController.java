package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController()
@RequestMapping("/api/")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    private ClientRepository clientRepository;

    @GetMapping("/transactions")
    public ResponseEntity<Object> getTransactions(Authentication authentication) {
        if (authentication.getName().contains("admin@admin.com")) {
            List<TransactionDTO> transactions = transactionRepository.findAll().stream()
                    .map(TransactionDTO::new)
                    .collect(toList());
            return new ResponseEntity<>(transactions, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/transactions/{code}")
    public ResponseEntity<Object> getTransaction(@PathVariable Long code, Authentication authentication) {
        Transaction transaction = transactionRepository.findById(code).orElse(null);
        if (transaction == null) {
            return new ResponseEntity<>("Recurso No encontrado", HttpStatus.BAD_GATEWAY);
        } else {
            if (transaction.getAccount().getMailHolder().equals(authentication.getName()) || authentication.getName().contains("admin@admin.com")) {
                return new ResponseEntity<>(new TransactionDTO(transaction), HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.BAD_GATEWAY);
            }
        }
    }
}
