package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RestController()
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/loans")
    public ResponseEntity<Object> getLoans(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null) {
            List<LoanDTO> loands = loanService.findAll();
            return new ResponseEntity<>(loands, HttpStatus.ACCEPTED);
        } else return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.FORBIDDEN);
    }

    @Transactional
    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> takeLoan(@RequestBody LoanApplicationDTO loan,
                                           Authentication authentication) {
        //{"loanId":1,"amount":12,"payments":12,"toAccountNumber":"VIN002"}

        if (loan.getLoanId() < 0) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (loan.getAmount() <= 0) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (loan.getPayments() < 0) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (loan.getToAccountNumber().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Loan loanType = loanService.findById(loan.getLoanId());
        if (loanType != null) { //verificamos si existe el tipo de prestamo
            if (loanType.isValid(loan)) { //validamos si prestamo cumple con sus caracteristicas
                if(loan.getAmount()>(loanType.getMaxAmount()/4)){//valido que el credito sea 1/4 mayor al maximo
                    Client client = clientService.findByEmail(authentication.getName());
                    if (client != null) { //verificamos que exista el cliente
                        if (client.isValidLoan(loanType,0)) {
                            //verificamos su el cliente puede adquir ese prestamo
                            //CONDICIONES:
                            //0: No mas de un tipo de credito
                            //1: limite de dinero por tipo de  prestamos
                            //2: cantidad de tipos de creditos: limite 2
                            //3: combinacion 1 y 2
                            Account account = accountService.findByNumber(loan.getToAccountNumber());
                            if (account.isValidClient(client.getEmail())) {//verificamos si la cuenta es del cliente
                                ClientLoan loanTaken = new ClientLoan(loan.getAmount(), loan.getPayments());
                                Transaction transaction = new Transaction(TransactionType.CREDIT, "Loan accreditation - TYPE: " + loanType.getName(), (float) loan.getAmount());
                                account.addTransaction(transaction);
                                loanTaken.addLoan(loanType);
                                loanTaken.addClient(client);
                                clientLoanService.save(loanTaken);
                                transactionService.save(transaction);
                                return new ResponseEntity<>("Loan create Total debt:"+ client.debtType(loanType), HttpStatus.CREATED);
                            } else {
                                return new ResponseEntity<>("Invalid Client", HttpStatus.FORBIDDEN);
                            }
                        } else {
                            return new ResponseEntity<>("client already has that type of loan", HttpStatus.FORBIDDEN);
                        }
                    } else {
                        return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.FORBIDDEN);
                    }
                }else{
                    return new ResponseEntity<>("Credito demasiado pequeño para este tipo", HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>("Invalid amount or payments", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Unknown loan type", HttpStatus.FORBIDDEN);

        }
    }


}
