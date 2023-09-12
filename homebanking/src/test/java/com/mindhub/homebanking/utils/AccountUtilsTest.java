package com.mindhub.homebanking.utils;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class AccountUtilsTest {

    @Test
    void newNumberAccount() {
        String numberAccount = AccountUtils.newNumberAccount();
        assertThat(numberAccount,is(not(emptyOrNullString())));
        assertThat(numberAccount.length(),is(12));
    }
}