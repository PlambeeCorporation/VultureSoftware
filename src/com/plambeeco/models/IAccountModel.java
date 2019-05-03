package com.plambeeco.models;

public interface IAccountModel {

    int getId();

    void setId(int id);

    String getUsername();

    String getPassword();

    IPersonModel getAccountOwner();

    AccountModel.AccountType getAccountType();

    void setUsername(String username);

    void setPassword(String password);

    void setAccountType(AccountModel.AccountType accountType);
}
