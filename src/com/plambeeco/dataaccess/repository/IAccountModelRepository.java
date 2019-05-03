package com.plambeeco.dataaccess.repository;

import com.plambeeco.models.IAccountModel;

public interface IAccountModelRepository extends IRepository<IAccountModel> {
    IAccountModel getAccount(String username, String password);
    void updatePassword(IAccountModel accountModel);
}
