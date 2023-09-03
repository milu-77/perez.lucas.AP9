package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Transaction findById(Long code) {
        return transactionRepository.findById(code).orElse(null);
    }

    @Override
    public List<TransactionDTO> findAll() {
        return transactionRepository.findAll().stream()
                .map(TransactionDTO::new)
                .collect(toList());
    }
}
