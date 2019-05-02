package com.plambeeco.view.accountviews;

import com.plambeeco.dataaccess.dataprocessor.AccountModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.helper.RegexHelper;
import com.plambeeco.helper.TextFieldHelper;
import com.plambeeco.models.AccountModel;
import com.plambeeco.models.IAccountModel;
import com.plambeeco.models.IPersonModel;
import com.plambeeco.models.PersonModel;
import com.plambeeco.view.RootHumanResourcesController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.stream.Stream;

import static com.plambeeco.models.AccountModel.AccountType;

public class CreateNewAccountController {
    @FXML
    private TextField txtForename;
    @FXML
    private TextField txtSurname;
    @FXML
    private TextField txtEmailAddress;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private ComboBox<String> cbAccountType;

    @FXML
    private void initialize(){
        TextFieldHelper.allowNumbersOnly(txtPhoneNumber);
        String[] accountTypesNames = Stream.of(AccountType.values()).map(AccountType::name).toArray(String[]::new);
        ObservableList<String> accountTypes = FXCollections.observableArrayList();
        accountTypes.addAll(accountTypesNames);
        cbAccountType.setItems(accountTypes);
        cbAccountType.setValue(accountTypes.get(0));
    }

    @FXML
    private void createAccount(){
        if(validateForm()){
            String forename = txtForename.getText();
            String surname = txtSurname.getText();
            String emailAddress = txtEmailAddress.getText();
            String phoneNumber = txtPhoneNumber.getText();
            IPersonModel personModel = new PersonModel(forename, surname, emailAddress, phoneNumber);
            PersonModelProcessor.add(personModel);

            String username = forename.charAt(0) + surname;
            String password = "password";
            String accountType = cbAccountType.getValue();
            IAccountModel account = new AccountModel(username, password, accountType, personModel);
            AccountModelProcessor.add(account);

            if(account.getUsername().equals(AccountModelProcessor.getAccount(username, password).getUsername())){
                AlertHelper.showAlert(RootHumanResourcesController.getPrimaryStage(), "New account created successfully", account.toString());
            }

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

        if(!isValid){
            AlertHelper.showAlert(RootHumanResourcesController.getPrimaryStage(), "Invalid Details", errorMessage);
        }

        return isValid;
    }
}
