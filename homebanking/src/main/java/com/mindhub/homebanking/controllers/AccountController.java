package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.utils.AccountUtils;
import com.mindhub.homebanking.utils.ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class AccountController<t> {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;


    @GetMapping("/accounts")
    public ResponseEntity<Object> getAccounts(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null) {
            if (client.isAdmin()) {
                List<AccountDTO> accounts = accountService.findAll();
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
        Account account = accountService.findById(code);
        if (account == null) {
            return new ResponseEntity<>("resource not found", HttpStatus.NOT_FOUND);
        } else {
            if ( account.isValidClient(authentication.getName())) {
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
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null) {
            return new ResponseEntity<>(new ClientDTO(client).getAccounts(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("resource not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/clients/current/accounts")
    public ResponseEntity<String> createAccount(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null) {
            if (client.canAccounts()) {
                String number = AccountUtils.newNumberAccount();
                while (accountService.findByNumber(number) != null) {
                    number = String.valueOf(AccountUtils.newNumberAccount());
                }
                Account account = new Account(number, 0);
                client.addAccounts(account);
                accountService.save(account);
                return new ResponseEntity<>("Account created, num accounts: "+client.numAccounts(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Account limit", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("User Account does not exists", HttpStatus.FORBIDDEN);
        }
    }

//    Eliminar cuentas 	Agregar botón para eliminar cuentas,
//    se deben eliminar las transacciones de la cuenta.
    @DeleteMapping (path = "clients/current/accounts/{accountsDelete}")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountsDelete,
                                             Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null) {
            Account account = accountService.findByNumber(accountsDelete);
            if (account != null) {
                if (ClientUtils.compareClientByMail(account.getHolder(), client)) {
                    account.setDeleted();
                    accountService.save(account);
                    return new ResponseEntity<>("Account delete", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("User account does not exists", HttpStatus.NOT_FOUND);
        }
    }











}

