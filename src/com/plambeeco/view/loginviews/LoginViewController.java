/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plambeeco.view.loginviews;


import com.plambeeco.VultureApplication;
import com.plambeeco.dataaccess.dataprocessor.AccountModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.models.IAccountModel;
import com.plambeeco.view.RootHumanResourcesController;
import com.plambeeco.view.RootTechnicianController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;



/**
 *
 * @author Neville Bulmer
 */
public class LoginViewController
{
    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField pfPassword;


    public LoginViewController() {
        initLoginLayout();
    }

    private void initLoginLayout()
    {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(VultureApplication.class.getResource("view/loginviews/loginview.fxml"));
            loader.setController(this);
            AnchorPane loginScene = loader.load();

            Scene scene = new Scene(loginScene);
            VultureApplication.getPrimaryStage().setScene(scene);
            VultureApplication.getPrimaryStage().show();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    private void login(){
        String username = txtLogin.getText();
        String password = pfPassword.getText();
        username = "TestTechnician";
        password = "password";

        if(validateLogin(username, password)){
            IAccountModel currentAccount = AccountModelProcessor.getAccount(username, password);

            if(currentAccount != null){
                switch(currentAccount.getAccountType()){
                    case Technician:
                        new RootTechnicianController(currentAccount);
                        break;
                    case HumanResources:
                        new RootHumanResourcesController(currentAccount);
                        break;
                }
            }else{
                AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Wrong login details", "You have entered wrong username or password!");
            }
        }


    }

    private boolean validateLogin(String username, String password){
        boolean isValid = true;
        String errorMessage = "";

        if(username.length() < 1){
            isValid = false;
            errorMessage += "Enter username\n";
        }

        if(password.length() < 1){
            isValid = false;
            errorMessage += "Enter password\n";
        }

        if(!isValid){
            AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Wrong login details", errorMessage);
        }
        return isValid;
    }
}



    

