package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);

    @Query("select (count(c) > 0) from Client c where c.email like concat('%', ?1, '%')")
    boolean existsByEmailContains(String email);


}
