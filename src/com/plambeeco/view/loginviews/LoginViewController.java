/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plambeeco.view.loginviews;


import com.plambeeco.VultureApplication;
import com.plambeeco.view.RootTechnicianController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;



/**
 *
 * @author Neville Bulmer
 */
public class LoginViewController
{
    private final String signupviewfile = "SignupView.fxml";
    private final String ROOT_TECHNICIAN_VIEW = "/com/plambeeco/view/roottechnicianview.fxml";
    private Stage primaryStage;

    public LoginViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    private void login(){
        new RootTechnicianController(primaryStage);
    }

    @FXML
    private void signup(){
        try{
            AnchorPane fxmlLoader = (AnchorPane) FXMLLoader.load(getClass().getResource(signupviewfile));
            Scene scene = new Scene(fxmlLoader);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception err) {
            err.printStackTrace();
        }
    }
}



    

