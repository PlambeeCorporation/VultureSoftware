package com.plambeeco.dataaccess.repository;

import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.helper.ConstantValuesHelper;
import com.plambeeco.models.AccountModel;
import com.plambeeco.models.IAccountModel;
import com.plambeeco.view.RootTechnicianController;

import java.sql.*;
import java.util.List;

public class AccountModelRepository implements IAccountModelRepository {
    private static final String TABLE_NAME = "Account";
    private static final String USERNAME_COLUMN = "Username";
    private static final String PASSWORD_COLUMN = "Password";
    private static final String ACCOUNT_TYPE_COLUMN = "AccountType";
    private static final String ACCOUNT_OWNER_ID = "AccountOwnerId";

    @Override
    public void add(IAccountModel accountModel) {
        final String checkSQL = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + USERNAME_COLUMN + " =?";

        final String sql =
                "INSERT INTO " + TABLE_NAME + "(" + USERNAME_COLUMN + "," + PASSWORD_COLUMN + ", " +
                        ACCOUNT_TYPE_COLUMN + ", " + ACCOUNT_OWNER_ID + ") " +
                        "VALUES(?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement psCheck = con.prepareStatement(checkSQL);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            psCheck.setString(1, accountModel.getUsername());
            ResultSet resultAlreadyExists = psCheck.executeQuery();

            if(!resultAlreadyExists.next()) {
                ps.setString(1, accountModel.getUsername());
                ps.setString(2, accountModel.getPassword());
                ps.setString(3, accountModel.getAccountType().toString());
                ps.setInt(4, accountModel.getAccountOwner().getPersonId());


                int affectedRows = ps.executeUpdate();

                if(affectedRows == 0){
                    throw new SQLException("Adding task failed, no rows affected");
                }
            }else{
                AlertHelper.showAlert(RootTechnicianController.getPrimaryStage(), "Error creating new account",
                        "Account with this username already exists");
            }
        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void update(IAccountModel item) {

    }

    @Override
    public void remove(IAccountModel item) {

    }

    @Override
    public IAccountModel getAccount(String username, String password) {
        final String sql = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + USERNAME_COLUMN + " =? AND " + PASSWORD_COLUMN + " =?";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, username);
            ps.setString(2, password);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new AccountModel(rs.getString(USERNAME_COLUMN),
                                                                  rs.getString(ACCOUNT_TYPE_COLUMN),
                            PersonModelProcessor.getById(rs.getInt(ACCOUNT_OWNER_ID)));
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve person details from the database: " + e.getMessage());
        }
        return null;
    }

    @Override
    public IAccountModel getById(int id) {
        return null;
    }

    @Override
    public List<IAccountModel> getAll() {
        return null;
    }
}
