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
public class AccountController<t> {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/api/accounts")
    public ResponseEntity<Object> getAccounts(Authentication authentication) {
        if (authentication.getName().contains("admin@admin.com")) {
            List<AccountDTO> accounts = accountRepository.findAll().stream()
                    .map(AccountDTO::new)
                    .collect(toList());
            return new ResponseEntity<>(accounts, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/api/accounts/{code}")
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

    @GetMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> getAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client != null) {
            return new ResponseEntity<>(new ClientDTO(client).getAccounts(), HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.BAD_GATEWAY);
        }
    }
    @RequestMapping(path = "/api/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<String> createAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client.numAccounts() < 3) {
            String number = Account.newNumberAccount();
            while (accountRepository.findByNumber(number) != null) number = String.valueOf(Account.newNumberAccount());
            Account account = new Account(number, 0);
            client.addAccounts(account);
            accountRepository.save(account);
            return new ResponseEntity<>("se pudo crear la cuenta, numero de cuentas: " + client.numAccounts() + " del cliente  " + client.getEmail(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Cliente con tres cuentas, imposible agregar mas", HttpStatus.FORBIDDEN);
        }
    }
}

