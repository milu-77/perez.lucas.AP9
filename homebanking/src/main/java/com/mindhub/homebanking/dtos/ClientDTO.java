package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ClientDTO  {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts = new HashSet<>();
    private Set<ClientLoanDTO>  clientLoanDTOS= new HashSet<>();





    public ClientDTO( Client  client ) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts=client.getAccounts().stream()
                                            .map(account -> new AccountDTO(account))
                                            .collect(Collectors.toSet());
        this.clientLoanDTOS=client.getClientLoans().stream()
                        .map(clientLoan -> new ClientLoanDTO(clientLoan))
                                .collect(Collectors.toSet());




    }

    public Set<AccountDTO>getAccounts() {
        return accounts;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<ClientLoanDTO> getloans() {
        return clientLoanDTOS;
    }
}
