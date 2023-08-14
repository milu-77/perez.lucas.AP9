package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@RestController()
@RequestMapping("/api/")
public class ClientController {

    @Autowired
    private ClientRepository personRepository;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return personRepository.findAll().stream()
                .map(ClientDTO::new)
                .collect(toList());
    }

    @GetMapping("/clients/{code}")
    public ClientDTO getClients(@PathVariable Long code) {

        return new ClientDTO(personRepository.findById(code).orElseThrow());
    }

}
