package com.plambeeco.models;

import java.util.Objects;

public class AccountModel implements IAccountModel {
    public enum AccountType {
        Technician,
        HumanResources,
        Client,
    }

    private int id;
    private String username;
    private String password;
    private AccountType accountType;
    private IPersonModel accountOwner;


    public AccountModel(String username, String password, String accountType, IPersonModel accountOwner) {
        this.username = username;
        this.password = password;
        this.accountType = AccountType.valueOf(accountType);
        this.accountOwner = accountOwner;
    }

    public AccountModel(String username, String accountType, IPersonModel accountOwner) {
        this.username = username;
        this.accountType = AccountType.valueOf(accountType);
        this.accountOwner = accountOwner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public IPersonModel getAccountOwner() {
        return accountOwner;
    }

    @Override
    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountModel that = (AccountModel) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return accountOwner.toString() + " | " + accountType;

    }
}
