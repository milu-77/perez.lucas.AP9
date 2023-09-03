package com.mindhub.homebanking.models;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private float maxAmount;
    @ElementCollection
    @Column(name = "payments")
    private List<Integer> payments = new ArrayList<>();
    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan() {
    }

    public Loan(String name, float maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxAmount(float maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    @JsonIgnore
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setLoan(ClientLoan client) {
        this.clientLoans.add(client);
        client.setLoan(this);
    }

    public boolean isValid(LoanApplicationDTO loan) {
        if(loan.getAmount()>this.maxAmount){
            return false;
        }
        return this.payments.contains(loan.getPayments());
    }
}

