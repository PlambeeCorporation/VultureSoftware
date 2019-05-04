package com.plambeeco.dataaccess.dataprocessor;

import com.plambeeco.dataaccess.repository.AccountModelRepository;
import com.plambeeco.dataaccess.repository.IAccountModelRepository;
import com.plambeeco.models.IAccountModel;

import java.util.List;

public class AccountModelProcessor {
    public static void add(IAccountModel accountModel){
        IAccountModelRepository accountModelRepository = new AccountModelRepository();
        accountModelRepository.add(accountModel);
    }

    public static IAccountModel getAccount(String username, String password){
        IAccountModelRepository accountModelRepository = new AccountModelRepository();
        return accountModelRepository.getAccount(username, password);
    }

    public static void update(IAccountModel accountModel){
        IAccountModelRepository accountModelRepository = new AccountModelRepository();
        accountModelRepository.update(accountModel);
    }

    public static void updatePassword(IAccountModel accountModel){
        IAccountModelRepository accountModelRepository = new AccountModelRepository();
        accountModelRepository.updatePassword(accountModel);
    }

    public static List<IAccountModel> getAll(){
        IAccountModelRepository accountModelRepository = new AccountModelRepository();
        return accountModelRepository.getAll();
    }
}
