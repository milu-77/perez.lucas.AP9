package com.mindhub.homebanking.models;

public enum CardColor {
    GOLD,
    SILVER,
    TITANIUM;

    public static CardColor returnColor(String cardColor) {
        if (cardColor.equalsIgnoreCase("GOLD")) {
            return CardColor.GOLD;
        }
        if (cardColor.equalsIgnoreCase("SILVER")) {
            return CardColor.SILVER;
        }
            return CardColor.TITANIUM;
    }
}
