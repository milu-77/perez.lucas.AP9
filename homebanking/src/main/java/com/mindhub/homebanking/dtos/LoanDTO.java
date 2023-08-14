package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;


import java.util.ArrayList;
import java.util.List;

public class LoanDTO {
    private Long id;

    private String name;
    private float maxAmount;

    private List<Integer> payments = new ArrayList<>();

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }
}
