package com.plambeeco.view.accountviews;

import com.plambeeco.VultureApplication;
import com.plambeeco.dataaccess.dataprocessor.AccountModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.helper.RegexHelper;
import com.plambeeco.helper.TextFieldHelper;
import com.plambeeco.models.IAccountModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class EditAccountsViewController {
    @FXML
    private ComboBox<IAccountModel> cbAccounts;
    @FXML
    private TextField txtForename;
    @FXML
    private TextField txtSurname;
    @FXML
    private TextField txtEmailAddress;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField pfOldPassword;
    @FXML
    private PasswordField pfNewPassword;
    @FXML
    private PasswordField pfConfirmPassword;
    private IAccountModel accountModel;

    @FXML
    private void initialize(){
        cbAccounts.setItems(FXCollections.observableList(AccountModelProcessor.getAll()));
        accountModel = cbAccounts.getItems().get(0);
        cbAccounts.setValue(accountModel);
        initializeFields();
        TextFieldHelper.allowNumbersOnly(txtPhoneNumber);
    }

    @FXML
    private void selectAccount(){
        accountModel = cbAccounts.getValue();
        initializeFields();
    }

    private void initializeFields(){
        txtForename.setText(accountModel.getAccountOwner().getForename());
        txtSurname.setText(accountModel.getAccountOwner().getSurname());
        txtEmailAddress.setText(accountModel.getAccountOwner().getEmailAddress());
        txtPhoneNumber.setText(accountModel.getAccountOwner().getPhoneNumber());
        txtUsername.setText(accountModel.getUsername());
    }

    @FXML
    private void confirm(){
        if(validateForm()){
            accountModel.getAccountOwner().setForename(txtForename.getText());
            accountModel.getAccountOwner().setSurname(txtSurname.getText());
            accountModel.getAccountOwner().setEmailAddress(txtEmailAddress.getText());
            accountModel.getAccountOwner().setPhoneNumber(txtPhoneNumber.getText());

            if(pfNewPassword.getLength() > 0){
                accountModel.setPassword(pfNewPassword.getText());
                AccountModelProcessor.updatePassword(accountModel);
            }

            if(!accountModel.getUsername().equals(txtUsername.getText())){
                accountModel.setUsername(txtUsername.getText());
                AccountModelProcessor.update(accountModel);
            }
            PersonModelProcessor.update(accountModel.getAccountOwner());
        }
    }

    private boolean validateForm(){
        boolean isValid = true;
        String errorMessage = "";

        if(txtForename.getLength() < 1){
            isValid = false;
            errorMessage += "Enter Forename!\n";
        }

        if(txtSurname.getLength() < 1){
            isValid = false;
            errorMessage += "Enter Surname!\n";
        }

        if(txtEmailAddress.getLength() > 0){
            if(!RegexHelper.validateEmailAddress(txtEmailAddress.getText())){
                isValid = false;
                errorMessage += "Invalid Email Address!\n";
            }
        }

        if(txtPhoneNumber.getLength() > 0){
            if(txtPhoneNumber.getLength() < 7){
                isValid = false;
                errorMessage += "Invalid Phone Number!\n";
            }
        }

        if(txtUsername.getLength() < 1){
            isValid = false;
            errorMessage += "Enter Username!\n";
        }

        if(pfNewPassword.getLength() > 0){
            if(!pfOldPassword.getText().equals(accountModel.getPassword())){
                isValid = false;
                errorMessage += "Incorrect password\n";
            }

            if(!pfNewPassword.getText().equals(pfConfirmPassword.getText())){
                isValid = false;
                errorMessage += "Confirm password doesn't match with the new password!";
            }
        }

        if(!isValid){
            AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Invalid Details", errorMessage);
        }

        return isValid;
    }
}
