package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByNumber(String number);

    @Query("select (count(a) > 0) from Account a where a.number like concat('%', ?1, '%')")
    boolean existsByNumberContains(String number);





 }
