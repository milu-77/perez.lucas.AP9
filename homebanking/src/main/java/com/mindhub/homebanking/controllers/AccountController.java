package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/")
public class AccountController<t> {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public ResponseEntity<Object> getAccounts(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client != null) {
            if (client.isAdmin()) {
                List<AccountDTO> accounts = accountRepository.findAll().stream()
                        .map(AccountDTO::new)
                        .collect(toList());
                return new ResponseEntity<>(accounts, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/accounts/{code}")
    public ResponseEntity<Object> getAccount(@PathVariable Long code, Authentication authentication) {
        Account account = accountRepository.findById(code).orElse(null);
        if (account == null) {
            return new ResponseEntity<>("resource not found", HttpStatus.NOT_FOUND);
        } else {
            if (account.getHolder().getEmail().equals(authentication.getName())) {
                AccountDTO accountDTO = new AccountDTO(account);
                return new ResponseEntity<>(accountDTO, HttpStatus.ACCEPTED);
            }
            if (account.getHolder().isAdmin()) {
                AccountDTO accountDTO = new AccountDTO(account);
                return new ResponseEntity<>(accountDTO, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @GetMapping("/clients/current/accounts")
    public ResponseEntity<Object> getAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client != null) {
            return new ResponseEntity<>(new ClientDTO(client).getAccounts(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("resource not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<String> createAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client != null) {
            if (client.numAccounts() < 3) {
                String number = Account.newNumberAccount();
                while (accountRepository.findByNumber(number) != null) {
                    number = String.valueOf(Account.newNumberAccount());
                }
                Account account = new Account(number, 0);
                client.addAccounts(account);
                accountRepository.save(account);
                return new ResponseEntity<>("Account created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Account limit", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("User Account does not exists", HttpStatus.FORBIDDEN);
        }
    }
}

