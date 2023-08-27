package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
 import java.util.Set;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Rol rol;

    @OneToMany(mappedBy = "holder", fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "cardHolder", orphanRemoval = true)
    private Set<Card> cards = new LinkedHashSet<>();


    public Client(String firstName, String lastName, String email, String encode) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = encode;
        this.rol = Rol.CLIENT;
    }
    public Client(String firstName, String lastName, String email, String encode,Rol rol) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = encode;
        this.rol = rol;
    }


    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }


    public Client() {
    }


    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccounts(Account account) {
        account.setHolder(this);
        this.accounts.add(account);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setLoan(ClientLoan loan) {
        this.clientLoans.add(loan);
        loan.setClient(this);
    }

    public void addCard(Card card1) {
        this.cards.add(card1);
        card1.setClient(this);
    }
    public int numAccounts (){
        return accounts.size();
    }

    public Long numCard(String cardType) {
        return cards.stream()
                .filter(card -> card.getCardType().name().equals(cardType))
               .count();
    }
    public boolean CanCard (String cardType) {
        long cantCard = cards.stream()
                .filter(card -> card.getCardType().name().equals(cardType))
                .count();
        return cantCard<3;
    }

    public boolean hasCards() {
        return !this.cards.isEmpty();
    }

    public boolean isAdmin() {
        return this.rol.ordinal()==0;
    }
}
