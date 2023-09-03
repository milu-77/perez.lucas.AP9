package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientLoanService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.LoanService;
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
        //control loan
      //{"loanId":1,"amount":12,"payments":12,"toAccountNumber":"VIN002"}
//        if( !loanRepository.existsById(loan.getLoanId())){
//            return new ResponseEntity<>("tipo de prestamo no reconococido" , HttpStatus.FORBIDDEN);
//
//        }
//        if(loan.getAmount()<=0){
//            return new ResponseEntity<>(" algo del dinero " , HttpStatus.FORBIDDEN);
//
//        }
//        if(loanRepository.findById( loan.getLoanId()).get().getPayments().contains(loan.getPayments())){
//            return new ResponseEntity<>(loanRepository.findById( loan.getLoanId()).get().getPayments() , HttpStatus.FORBIDDEN);
//
//        }





        Client client = clientService.findByEmail(authentication.getName());
        ClientLoan loanTaken = new ClientLoan(loan.getAmount(), loan.getPayments());
        Loan type = loanService.findById(loan.getLoanId());
        Account account = accountService.findByNumber(loan.getToAccountNumber());
        account.addLoan(loanTaken);
        loanTaken.addLoan(type);
        loanTaken.addClient(client);
        clientLoanService.save(loanTaken);



//        if (client != null) {
//            //continuara....
//            return new ResponseEntity<>("requestLoan", HttpStatus.ACCEPTED);
//        } else {
//            return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.FORBIDDEN);
//        }
             return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.CREATED);

    }


}
