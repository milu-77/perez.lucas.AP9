package com.mindhub.homebanking.models;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Client(String firstName, String lastName, String email, String encode, Rol rol) {
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

    public int numAccounts() {
        return accounts.size();
    }

    public Long numCard(CardType cardType) {
        Set<Card> cards0 = this.cards.stream()
                .filter(newCard -> !newCard.isDeleted())
                .collect(Collectors.toSet());

        return cards0.stream()
                .filter(card -> card.getCardType().equals(cardType))
                .count();
    }

    public boolean canCard(CardType cardType, CardColor cardColor) {
        Set<Card> cards0 = this.cards.stream()
                .filter(newCard -> !newCard.isDeleted())
                .collect(Collectors.toSet());

        Set<Card> cards = cards0.stream()
                .filter(newCard -> newCard.getCardType() == cardType).collect(Collectors.toSet());
        long cantColor = cards.stream()
                .filter(card -> card.getCardColor().equals(cardColor))
                .count();
        return cantColor == 0 && cards.size() < 3;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean hasCards() {
        return !this.cards.isEmpty();
    }

    public boolean isAdmin() {
        return this.rol.ordinal() == 0;
    }



    public boolean isValidLoan(Loan loanType, int condition) {
        //int condition =0;
        switch (condition) {
            case 0: // No mas de un tipo de credito
                return existLoanType(loanType);
            case 1:// Limite de dinero
                return this.debtType(loanType) < loanType.getMaxAmount();
            case 2: //cantidad de tipos de creditos: limite 2
                 return this.cantDebType(loanType)<2;
            case 3: //cantidad de tipos de creditos: limite 2 y no mas del maximo
                return this.cantDebType(loanType)<2 &&  this.debtType(loanType)<loanType.getMaxAmount();
            default:
                return existLoanType(loanType);
        }
     }


    public boolean canAccounts() {
        return numAccounts()<3;
    }

    public boolean isValidAuthentication(String email) {
        return this.getEmail().equals(email);
    }
    public double debtType(Loan loanType){
        List<ClientLoan> loan = this.getClientLoans()
                .stream()
                .filter(clientLoan -> clientLoan.getLoan().getName().equals(loanType.getName()))
                .collect(Collectors.toList());
        double total=0;
        for (ClientLoan elemento : loan) {
            total=total+elemento.getAmount();
        }
        return total;
    }

    public Long cantDebType(Loan loanType){
        Long  cant = this.getClientLoans()
                .stream()
                .filter(clientLoan -> clientLoan.getLoan().getName().equals(loanType.getName()))
                .count();
        return cant;
    }
    public boolean existLoanType (Loan loanType){
        return this.getClientLoans()
                .stream()
                .noneMatch(clientLoan -> clientLoan.getLoan().getName().equals(loanType.getName()));
    }



}
