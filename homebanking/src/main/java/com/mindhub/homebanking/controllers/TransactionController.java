package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/transactions")
    public ResponseEntity<Object> getTransactions(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if(client != null){
            if (client.isAdmin()) {
                List<TransactionDTO> transactions = transactionService.findAll();
                return new ResponseEntity<>(transactions, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
            }
        }
         else {
            return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/transactions/{code}")
    public ResponseEntity<Object> getTransaction(@PathVariable Long code, Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if(client != null){
             Transaction transaction = transactionService.findById(code);
            if (transaction == null) {
                return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
            } else {

                if (transaction.isHolder(client ) ||  client.isAdmin()) {
                    return new ResponseEntity<>(new TransactionDTO(transaction), HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
                }
            }
        } else {
            return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
        }









    }

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransactions(@RequestParam String fromAccountNumber,
                                                     @RequestParam String toAccountNumber,
                                                     @RequestParam float amount,
                                                     @RequestParam String description,
                                                     Authentication authentication) {
        if (fromAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (amount <= 0) {
            return new ResponseEntity<>("Error Amount", HttpStatus.FORBIDDEN);
        }
        if (description.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Same account", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null) {
            Account accountFrom = accountService.findByNumber(fromAccountNumber);
            if (accountFrom != null) {
                if (accountFrom.getHolder().getEmail().equals(client.getEmail())) {
                    Account accountTO = accountService.findByNumber(toAccountNumber);
                    if (accountTO != null) {
                        if (accountFrom.getBalance() >= amount) {
                            Transaction transactionCredit = new Transaction(TransactionType.CREDIT, description, amount);
                            Transaction transactionDebit = new Transaction(TransactionType.DEBIT, description, amount);
                            accountFrom.addTransaction(transactionDebit);
                            //int a = 2 / 0;
                            accountTO.addTransaction(transactionCredit);
                            accountService.save(accountFrom);
                            accountService.save(accountTO);
                            transactionService.save(transactionCredit);
                            transactionService.save(transactionDebit);
                            return new ResponseEntity<>("successful transaction", HttpStatus.ACCEPTED);
                        } else {
                            return new ResponseEntity<>("No credit", HttpStatus.FORBIDDEN);
                        }
                    } else {
                        return new ResponseEntity<>("Account to... not found", HttpStatus.FORBIDDEN);
                    }
                } else {
                    return new ResponseEntity<>("Not account owner", HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>("Account from... not found", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("User Account does not exists ", HttpStatus.FORBIDDEN);
        }
    }

}
