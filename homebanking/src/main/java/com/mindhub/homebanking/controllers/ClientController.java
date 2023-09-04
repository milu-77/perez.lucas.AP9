package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController()
@RequestMapping("/api/")
public class ClientController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;


    @GetMapping("/clients")
    public ResponseEntity<Object> getClients(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null) {
            if (client.isAdmin()) {
                List<ClientDTO> clients = clientService.getClientDTO();
                return new ResponseEntity<>(clients, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        return clientService.findDTOByEmail(authentication.getName());
    }

    @GetMapping("/clients/{code}")
    public ResponseEntity<Object> getClients(@PathVariable Long code, Authentication authentication) {//ClientDTO
        Client client = clientService.findById(code);
        if (client == null) {
            return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
        } else {
            if (client.isValidAuthentication(authentication.getName() ) ) {
                return new ResponseEntity<>(new ClientDTO(client), HttpStatus.ACCEPTED);
            }
            if (client.isAdmin()) {
                return new ResponseEntity<>(new ClientDTO(client), HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("You don't have permission to access on this server", HttpStatus.UNAUTHORIZED);
            }
        }
    }


    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (lastName.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (email.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        String number = Account.newNumberAccount();
        while (accountService.findByNumber(number) != null) number = String.valueOf(Account.newNumberAccount());
        Account account = new Account(number, 0);
        client.addAccounts(account);
        clientService.save(client);
        accountService.save(account);
        return new ResponseEntity<>("User Account created", HttpStatus.CREATED);
    }

}
