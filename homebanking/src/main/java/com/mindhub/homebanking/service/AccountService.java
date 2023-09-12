package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account findByNumber(String number);

    void save(Account account);

    List<AccountDTO> findAll();

    Account findById(Long code);


    boolean existsByNumber(String number);
}
