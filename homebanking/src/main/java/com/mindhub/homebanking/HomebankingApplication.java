package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@SpringBootApplication
public class HomebankingApplication {


    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository,
                                      AccountRepository accountRepository,
                                      TransactionRepository transactionRepository,
                                      LoanRepository loanRepository,
                                      ClientLoanRepository clientLoanRepository,
                                      CardRepository cardRepository) {
        return (args) -> {
            //OBJETOS
            Client melba = new Client("Melba ", "Morel ", "melba@morel.com", passwordEncoder.encode("123"),Rol.CLIENT);
            Client juan = new Client("Juan", "Salvo ", "juan@salvo.com", passwordEncoder.encode("123"),Rol.CLIENT);
            Client martita = new Client("Martita", "Salvo ", "martita@salvo.com", passwordEncoder.encode("123"),Rol.CLIENT);
            Client admin = new Client("admin ", "admin ", "admin@admin.com", passwordEncoder.encode("123"),Rol.ADMIN);

            Account vin001 = new Account("VIN001", LocalDateTime.now().minusDays(10), 10000);
            Account vin002 = new Account("VIN002", LocalDateTime.now().minusDays(50), 10000);
            Account vin003 = new Account("VIN003", LocalDateTime.now().minusDays(48), 10000);

            Transaction transaction1 = new Transaction(TransactionType.DEBIT, LocalDateTime.now().minusDays(2), "Compra", 1000);
            Transaction transaction2 = new Transaction(TransactionType.CREDIT, LocalDateTime.now().minusDays(2), "Transferencia", 1000);
            Transaction transaction3 = new Transaction(TransactionType.DEBIT, LocalDateTime.now().minusDays(2), "Gasto Random", 5000);
            Transaction transaction4 = new Transaction(TransactionType.CREDIT, LocalDateTime.now().minusDays(2), "Transferencia", 1000);
            Transaction transaction5 = new Transaction(TransactionType.DEBIT, LocalDateTime.now().minusDays(1), "Compra de algo random", 2000);
            Transaction transaction6 = new Transaction(TransactionType.CREDIT, LocalDateTime.now().minusDays(5), "Transferencia", 1000);

            Loan hipotecario = new Loan("mortgage", 500000, new ArrayList<Integer>() {{
                add(12);
                add(24);
                add(36);
                add(48);
                add(60);
            }});
            Loan personal = new Loan("Personal Loan", 500000, new ArrayList<Integer>() {{
                add(6);
                add(12);
                add(24);
            }});
            Loan automotriz = new Loan("Auto loan", 500000, new ArrayList<Integer>() {{
                add(6);
                add(12);
                add(24);
                add(36);
            }});
            ClientLoan prestamo1 = new ClientLoan(400000, 60);
            ClientLoan prestamo2 = new ClientLoan(50000, 12);
            ClientLoan prestamo3 = new ClientLoan(100000, 24);
            ClientLoan prestamo4 = new ClientLoan(200000, 36);

            Card card1 = new Card(CardType.DEBIT, CardColor.GOLD, LocalDateTime.now());
            Card card2 = new Card(CardType.CREDIT, CardColor.TITANIUM, "4117-9603-8636-9162", "456", LocalDateTime.now().plusYears(5), LocalDateTime.now());
            Card card3 = new Card(CardType.CREDIT, CardColor.SILVER, LocalDateTime.now());


            prestamo1.addClient(melba);
            prestamo1.addLoan(hipotecario);
            prestamo2.addClient(melba);
            prestamo2.addLoan(personal);
            prestamo3.addClient(juan);
            prestamo3.addLoan(personal);
            prestamo4.addClient(juan);
            prestamo4.addLoan(automotriz);

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
            melba.addCard(card1);
            melba.addCard(card2);
            juan.addCard(card3);

            //INGRESO A REPOSITORIOS
            clientRepository.save(melba);
            clientRepository.save(juan);
            clientRepository.save(martita);
            clientRepository.save(admin);
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
            cardRepository.save(card1);
            cardRepository.save(card2);
            cardRepository.save(card3);


        };
    }

}