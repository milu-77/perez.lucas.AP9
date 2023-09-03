package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    void save(Transaction transaction);

    Transaction findById(Long code);

    List<TransactionDTO> findAll();
}
