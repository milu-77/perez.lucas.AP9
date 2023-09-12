package com.mindhub.homebanking.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

class CardUtilsTest {

    @Test
    void generateCvn() {
        String cvn = CardUtils.generateCvn();
        assertThat(cvn,is(not(emptyOrNullString())));
        assertThat(cvn.length(),is(3));
    }

    @Test
    void generateTruDate() {
        LocalDateTime date0 = LocalDateTime.now();
        LocalDateTime date1 =CardUtils.generateTruDate(date0,10);
        assertThat(date1,is(notNullValue()));
        assertThat(date1,is(date0.plusYears(10)));
    }

    @Test
    void newNumberCard() {
        String numberCard = CardUtils.newNumberCard();
        assertThat(numberCard,is(not(emptyOrNullString())));
        assertThat(numberCard.length(),is(19));
    }
}