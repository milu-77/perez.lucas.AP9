package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository repo2) {
		return (args) -> {
			// save a couple of customers
			Client melba =new Client("Melba ", "Morel ","Melba@morel.com");
			Client juan =new Client("Juan", "Salvo ","juan@salvo.com");
			Client martita = new Client("Martita", "Salvo ","martita@salvo.com");

			Account vin001 = new Account( "VIN001",LocalDateTime.now(),5000);
			Account vin002 = new Account( "VIN002",LocalDateTime.now().plusHours(24),7500);
			Account vin003 = new Account( "VIN003",LocalDateTime.now().plusHours(48),5000);
			melba.addAccounts(vin001);
			melba.addAccounts(vin002);
			juan.addAccounts(vin003);
			repository.save(melba);
			repository.save(juan);
			repository.save(martita);
			repo2.save(vin001);
			repo2.save(vin002);
			repo2.save(vin003);

		};
	}
}
