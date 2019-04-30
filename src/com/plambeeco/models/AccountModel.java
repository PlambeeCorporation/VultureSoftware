package com.plambeeco.models;

public class AccountModel implements IAccountModel {
    public enum AccountType {
        Technician,
    }

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
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
