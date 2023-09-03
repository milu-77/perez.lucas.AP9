package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private double amount;
    private int payments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;


    public ClientLoan() {
    }

    public ClientLoan(double amount, int payments) {
        this.amount = amount;
        this.payments = payments;
    }

    public ClientLoan(double amount, int payments, Client client, Loan loan) {
        this.amount = amount;
        this.payments = payments;
        this.client = client;
        client.setLoan(this);
        this.loan = loan;
        loan.setLoan(this);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public Client getClient() {
        return client;
    }

    public Loan getLoan() {
        return loan;
    }

    public Long getId() {
        return id;
    }

    public void addClient(Client client) {
        this.client = client;
        client.setLoan(this);
    }

    public void addLoan(Loan loan) {
        this.loan = loan;
        loan.setLoan(this);
    }
}
