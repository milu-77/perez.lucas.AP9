package com.mindhub.homebanking.models;

public enum CardType {
    CREDIT,
    DEBIT;

    static  public CardType returnType(String type) {
        if(type.equalsIgnoreCase("CREDIT")){
            return CardType.CREDIT;
        }else{
            return CardType.DEBIT;
        }
    }
}
