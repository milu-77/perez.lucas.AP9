package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    ClientRepository clientRepository;


    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public List<ClientDTO> getClientDTO() {
        return clientRepository.findAll().stream()
                .map(ClientDTO::new)
                .collect(toList());
    }

    @Override
    public Client findById(Long code) {
        return clientRepository.findById(code).orElse(null);
    }

    @Override
    public void save(Client client) {
        clientRepository.save(client);
    }
}
