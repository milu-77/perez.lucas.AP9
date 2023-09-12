package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<AccountDTO> findAll() {
        return accountRepository.findAll().stream()
                .map(AccountDTO::new)
                .collect(toList());
    }

    @Override
    public Account findById(Long code) {
        return accountRepository.findById(code).orElse(null);
    }

    @Override
    public boolean existsByNumber(String number) {
        return accountRepository.existsByNumberContains(number);
    }



}
