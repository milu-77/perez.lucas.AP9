package com.mindhub.homebanking.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void addTransaction() {
        Account account = new Account("test", 10000);
        Transaction transaction1 = new Transaction(TransactionType.DEBIT, LocalDateTime.now().minusDays(2), "Compra", 9000);
        account.addTransaction(transaction1);
        float res=1000;
        assertThat(account.getBalance(),is(res));
        Transaction transaction2 = new Transaction(TransactionType.CREDIT, LocalDateTime.now().minusDays(2), "venta", 10000);
        account.addTransaction(transaction2);
        res=res+10000;
        assertThat(account.getBalance(),is(res));
    }
}