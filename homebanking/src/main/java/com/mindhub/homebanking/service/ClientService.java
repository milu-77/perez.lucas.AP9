package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {


    Client findByEmail(String name);

    ClientDTO findDTOByEmail(String email);

    List<ClientDTO> getClientDTO();

    Client findById(Long code);

    void save(Client client);
}
