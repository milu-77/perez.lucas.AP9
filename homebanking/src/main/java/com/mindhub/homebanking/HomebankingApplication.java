package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository,
                                      AccountRepository accountRepository,
                                      TransactionRepository transactionRepository,
                                      LoanRepository loanRepository,
                                      ClientLoanRepository clientLoanRepository) {
        return (args) -> {
            // save a couple of customers
            //OBJETOS
            Client melba = new Client("Melba ", "Morel ", "Melba@morel.com");
            Client juan = new Client("Juan", "Salvo ", "juan@salvo.com");
            Client martita = new Client("Martita", "Salvo ", "martita@salvo.com");

            Account vin001 = new Account("VIN001", LocalDateTime.now(), 5000);
            Account vin002 = new Account("VIN002", LocalDateTime.now().plusHours(24), 7500);
            Account vin003 = new Account("VIN003", LocalDateTime.now().plusHours(48), 5000);

            Transaction transaction1 = new Transaction(TransactionType.DEBIT, LocalDateTime.now(), "Compra", 1000);
            Transaction transaction2 = new Transaction(TransactionType.CREDIT, LocalDateTime.now().plusHours(2), "Transferencia", 1000);
            Transaction transaction3 = new Transaction(TransactionType.DEBIT, LocalDateTime.now().plusMonths(2), "Gasto Random", 5000);
            Transaction transaction4 = new Transaction(TransactionType.CREDIT, LocalDateTime.now().plusHours(2), "Transferencia", 1400);
            Transaction transaction5 = new Transaction(TransactionType.DEBIT, LocalDateTime.now().plusHours(1), "Compra de algo random", 1030);
            Transaction transaction6 = new Transaction(TransactionType.CREDIT, LocalDateTime.now().plusHours(2), "Transferencia", 1000);

            Loan hipotecario = new Loan("Hipotecario", 500000, new ArrayList<Integer>() {{
                add(12);
                add(24);
                add(36);
                add(48);
                add(60);
            }});
            Loan personal = new Loan("Personal", 500000, new ArrayList<Integer>() {{
                add(6);
                add(12);
                add(24);
            }});
            Loan automotriz = new Loan("Automotriz", 500000, new ArrayList<Integer>() {{
                add(6);
                add(12);
                add(24);
                add(36);
            }});
            ClientLoan prestamo1 = new ClientLoan(400000, 60);
            ClientLoan prestamo2 = new ClientLoan(50000, 12);
            ClientLoan prestamo3 = new ClientLoan(100000, 24, juan, personal);
            ClientLoan prestamo4 = new ClientLoan(200000, 36, juan, automotriz);

            prestamo1.addClient(melba);
            prestamo1.addLoan(hipotecario);
            prestamo2.addClient(melba);
            prestamo2.addLoan(personal);

            //RELACIONES
            vin001.addTransaction(transaction1);
            vin001.addTransaction(transaction2);
            vin001.addTransaction(transaction3);
            vin002.addTransaction(transaction4);
            vin002.addTransaction(transaction5);
            vin002.addTransaction(transaction6);
            melba.addAccounts(vin001);
            melba.addAccounts(vin002);
            juan.addAccounts(vin003);
            //INGRESO A REPOSITORIOS
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
            loanRepository.save(hipotecario);
            loanRepository.save(personal);
            loanRepository.save(automotriz);
            clientLoanRepository.save(prestamo1);
            clientLoanRepository.save(prestamo2);
            clientLoanRepository.save(prestamo3);
            clientLoanRepository.save(prestamo4);


        };
    }
}
