package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@RestController()
@RequestMapping("/api/")
public class ClientController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/clients")
    public ResponseEntity<Object> getClients(Authentication authentication) {
        Client client =  clientRepository.findByEmail(authentication.getName());
        if (client!=null){
            if (client.isAdmin()) {
                List<ClientDTO> clients = clientRepository.findAll().stream()
                        .map(ClientDTO::new)
                        .collect(toList());
                return new ResponseEntity<>(clients, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @GetMapping("/clients/{code}")
    public ResponseEntity<Object> getClients(@PathVariable Long code, Authentication authentication) {//ClientDTO
        Client client = clientRepository.findById(code).orElse(null);
        if (client == null) {
            return new ResponseEntity<>("Recurso No encontrado", HttpStatus.BAD_GATEWAY);
        } else {
            if (client.getEmail().equals(authentication.getName())) {
                ClientDTO ClientDTO = new ClientDTO(client);
                return new ResponseEntity<>(ClientDTO, HttpStatus.ACCEPTED);
            }
            if (client.isAdmin()) {
                ClientDTO ClientDTO = new ClientDTO(client);
                return new ResponseEntity<>(ClientDTO, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.UNAUTHORIZED);
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
        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        String number = Account.newNumberAccount();
        while (accountRepository.findByNumber(number) != null) number = String.valueOf(Account.newNumberAccount());
        Account account = new Account(number, 0);
        client.addAccounts(account);
        clientRepository.save(client);
        accountRepository.save(account);
        return new ResponseEntity<>("Cuenta de usuario Creada", HttpStatus.CREATED);
    }

}
