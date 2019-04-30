package com.plambeeco.models;

public interface IAccountModel {
    String getUsername();

    String getPassword();

    IPersonModel getAccountOwner();

    AccountModel.AccountType getAccountType();

    void setUsername(String username);

    void setAccountType(AccountModel.AccountType accountType);
}
