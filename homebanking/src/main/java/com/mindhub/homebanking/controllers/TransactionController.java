package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionRepository.findAll().stream()
                .map(TransactionDTO::new)
                .collect(toList());
    }

    @GetMapping("/transactions/{code}")
    public TransactionDTO getTransaction(@PathVariable Long code) {

        return new TransactionDTO(transactionRepository.findById(code).orElseThrow());
    }
}
