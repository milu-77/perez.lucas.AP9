package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @RequestMapping( "/api/accounts")
    public List<Account> getClients () {
        List<Account> account = accountRepository.findAll();
        return account;
    }

    @RequestMapping ( "/api/accounts/{code}")
    public Optional<Account> getClients (@PathVariable Long code) {
        Optional<Account> account = accountRepository.findById(code);
        return account;
    }
}
