package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class AccountController<t> {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        List<AccountDTO> accounts = accountRepository.findAll().stream()
                .map(AccountDTO::new)
                .collect(toList());
        ;
        return accounts;
    }

    @RequestMapping("/api/accounts/{code}")
    public ResponseEntity<Object> getAccount(@PathVariable Long code, Authentication authentication) {
        Account account = accountRepository.findById(code).orElse(null);
        if (account == null) {
            return new ResponseEntity<>("Recurso No encontrado", HttpStatus.BAD_GATEWAY);
        } else {
            if (account.getHolder().getEmail().equals(authentication.getName()) || authentication.getName().contains("admin@admin.com")) {
                AccountDTO accountDTO = new AccountDTO(account);
                return new ResponseEntity<>(accountDTO, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.BAD_GATEWAY);
            }
        }
    }
}

