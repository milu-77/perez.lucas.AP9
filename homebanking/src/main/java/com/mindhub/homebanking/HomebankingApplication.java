package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository) {
		return (args) -> {
			// save a couple of customers
			Client melba =new Client("Melba ", "Morel ","Melba@morel.com");
			Client juan =new Client("Juan", "Salvo ","juan@salvo.com");
			Client martita = new Client("Martita", "Salvo ","martita@salvo.com");

			Account vin001 = new Account( "VIN001",LocalDateTime.now(),5000);
			Account vin002 = new Account( "VIN002",LocalDateTime.now().plusHours(24),7500);
			Account vin003 = new Account( "VIN003",LocalDateTime.now().plusHours(48),5000);

			Transaction transaction1 = new Transaction( TransactionType.DEBIT,LocalDateTime.now(),"Compra",1000 );
			Transaction transaction2 = new Transaction( TransactionType.CREDIT,LocalDateTime.now().plusHours(2),"Transferencia",1000 );
			Transaction transaction3 = new Transaction( TransactionType.DEBIT,LocalDateTime.now().plusMonths(2),"Gasto Random",5000 );
			Transaction transaction4 = new Transaction( TransactionType.CREDIT,LocalDateTime.now().plusHours(2),"Transferencia",1400 );
			Transaction transaction5 = new Transaction( TransactionType.DEBIT,LocalDateTime.now().plusHours(1),"Comprra de algo random",1030 );
			Transaction transaction6 = new Transaction( TransactionType.CREDIT,LocalDateTime.now().plusHours(2),"Transferencia",1000 );

			vin001.addTransaction(transaction1);
			vin001.addTransaction(transaction2);
			vin001.addTransaction(transaction3);
			vin002.addTransaction(transaction4);
			vin002.addTransaction(transaction5);
			vin002.addTransaction(transaction6);
			melba.addAccounts(vin001);
			melba.addAccounts(vin002);
			juan.addAccounts(vin003);



			clientRepository.save(melba);
			clientRepository.save(juan);
			clientRepository.save(martita);
			accountRepository.save(vin001);
			accountRepository.save(vin002);
			accountRepository.save(vin003);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);


		};
	}
}
