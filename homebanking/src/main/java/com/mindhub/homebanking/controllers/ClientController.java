package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
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
    private ClientRepository personRepository;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return personRepository.findAll().stream()
                .map(ClientDTO::new)
                .collect(toList());
    }

    @GetMapping("/clients/current")
    public ClientDTO getClients(Authentication authentication) {
        return new ClientDTO(personRepository.findByEmail(authentication.getName()));
    }

    @GetMapping("/clients/{code}")
    public ResponseEntity<Object> getClients(@PathVariable Long code, Authentication authentication) {//ClientDTO
        Client client = personRepository.findById(code).orElse(null);
        if (client == null) {
            return new ResponseEntity<>("Recurso No encontrado", HttpStatus.BAD_GATEWAY);
        } else {
            if (client.getEmail().equals(authentication.getName()) || authentication.getName().contains("admin@admin.com")) {
                ClientDTO ClientDTO = new ClientDTO(client);
                return new ResponseEntity<>(ClientDTO, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("No tiene permiso de acceso a este recurso", HttpStatus.BAD_GATEWAY);
            }
        }
    }


    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (personRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        personRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
