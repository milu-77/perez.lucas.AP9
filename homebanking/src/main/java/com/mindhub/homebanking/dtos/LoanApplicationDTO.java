package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    //{"loanId":1,"amount":12,"payments":12,"toAccountNumber":"VIN002"}

    Long loanId;
    double amount;
    int payments;
    String toAccountNumber;


    public LoanApplicationDTO() {
    }

    public Long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }


}
