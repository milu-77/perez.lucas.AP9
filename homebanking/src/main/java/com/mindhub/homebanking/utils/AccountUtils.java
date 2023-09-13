package com.mindhub.homebanking.utils;

import java.time.LocalDateTime;
import java.util.Random;

public class AccountUtils {
    public static String newNumberAccount() {
        //PROBLEMA: Se genera solo 1000 cuentas al dia
        StringBuilder number = new StringBuilder();
        number.append("VIN-");
        LocalDateTime date = LocalDateTime.now();
        number.append(date.getYear() % 100);
        Random random = new Random();
        number.append(random.nextInt(900000)+100000);
        return String.valueOf(number);
    }
}
