package com.mindhub.homebanking.utils;

import java.time.LocalDateTime;
import java.util.Random;

public class CardUtils {
    public static String generateCvn() {
        StringBuilder cvn = new StringBuilder();
        Random random = new Random();
        for (int a = 0; a < 3; a++) {
            int randomNumber = random.nextInt(10);
            cvn.append(randomNumber);
        }
        return String.valueOf(cvn);
    }

    public static LocalDateTime generateTruDate(LocalDateTime fromDate, int years) {
        return fromDate.plusYears(years);
    }

    public static String newNumberCard() {
        Random random = new Random();
        String number = (random.nextInt(9000) + 1000) +
                "-" +
                (random.nextInt(9000) + 1000) +
                "-" +
                (random.nextInt(9000) + 1000) +
                "-" +
                (random.nextInt(9000) + 1000);

        return number;
    }





}
