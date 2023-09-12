package com.mindhub.homebanking.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void debtType() {
        Client client =new Client();
        Loan hipotecario = new Loan("mortgage", 500000, new ArrayList<Integer>() {{
            add(12);
            add(24);
            add(36);
            add(48);
            add(60);
        }});
        ClientLoan prestamo1 = new ClientLoan(200000, 60);
        prestamo1.addClient(client);
        prestamo1.addLoan(hipotecario);
        double restEsperado=200000;
        assertThat( client.debtType(hipotecario),is(restEsperado));
        ClientLoan prestamo2 = new ClientLoan(200000, 60);
        prestamo2.addClient(client);
        prestamo2.addLoan(hipotecario);
        restEsperado=400000;
        assertThat( client.debtType(hipotecario),is(restEsperado));





    }

    @Test
    void cantDebType() {
        Client client =new Client();
        Loan hipotecario = new Loan("mortgage", 500000, new ArrayList<Integer>() {{
            add(12);
            add(24);
            add(36);
            add(48);
            add(60);
        }});
        ClientLoan prestamo1 = new ClientLoan(200000, 60);
        prestamo1.addClient(client);
        prestamo1.addLoan(hipotecario);
        assertThat( client.cantDebType(hipotecario),is(1L));
        ClientLoan prestamo2 = new ClientLoan(200000, 60);
        prestamo2.addClient(client);
        prestamo2.addLoan(hipotecario);
        assertThat( client.cantDebType(hipotecario),is(2L));
    }

    @Test
    void existLoanType() {
        Client client =new Client();
        Loan hipotecario = new Loan("mortgage", 500000, new ArrayList<Integer>() {{
            add(12);
            add(24);
            add(36);
            add(48);
            add(60);
        }});
        assertThat( client.existLoanType(hipotecario),is(true));
        ClientLoan prestamo1 = new ClientLoan(200000, 60);
        prestamo1.addClient(client);
        prestamo1.addLoan(hipotecario);
        assertThat( client.existLoanType(hipotecario),is(false));
    }


    @Test
    void canCard() {
        Card card1 = new Card(CardType.CREDIT, CardColor.GOLD);
        Card card2 = new Card(CardType.CREDIT, CardColor.TITANIUM);
        Card card3 = new Card(CardType.CREDIT, CardColor.SILVER);
        Client client =new Client();
        client.addCard(card1);
        assertThat( client.canCard(CardType.CREDIT, CardColor.GOLD),is(false));
        assertThat( client.canCard(CardType.CREDIT, CardColor.TITANIUM),is(true));
        assertThat( client.canCard(CardType.CREDIT, CardColor.SILVER),is(true));
        client.addCard(card2);
        assertThat( client.canCard(CardType.CREDIT, CardColor.GOLD),is(false));
        assertThat( client.canCard(CardType.CREDIT, CardColor.TITANIUM),is(false));
        assertThat( client.canCard(CardType.CREDIT, CardColor.SILVER),is(true));
        client.addCard(card3);
        assertThat( client.canCard(CardType.CREDIT, CardColor.GOLD),is(false));
        assertThat( client.canCard(CardType.CREDIT, CardColor.TITANIUM),is(false));
        assertThat( client.canCard(CardType.CREDIT, CardColor.SILVER),is(false));
    }
}