/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plambeeco.view.loginviews;

import com.plambeeco.ScreensController;
import com.plambeeco.view.RootTechnicianController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Neville Bulmer
 */
public class LoginViewController implements Initializable
{
    ScreensController myController;

    private Stage primaryStage = new Stage();

    @FXML
    private Button loginButton;
    
    @FXML
    private Label goToSignup;

    public static String signupviewfile = "SignupView.fxml";

    public static String mainviewfile = "/com/plambeeco/view/roottechnicianview.fxml";

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        loginButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override 
            public void handle(ActionEvent e) 
            {
                try{
                    BorderPane fxmlLoader = (BorderPane) FXMLLoader.load(getClass().getResource(mainviewfile));
                    Scene scene = new Scene(fxmlLoader);

                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch(Exception err) {
                    err.printStackTrace();
                }
            }
        });

        goToSignup.setOnMouseClicked(event ->
        {
            try{
                AnchorPane fxmlLoader = (AnchorPane) FXMLLoader.load(getClass().getResource(signupviewfile));
                Scene scene = new Scene(fxmlLoader);

                primaryStage.setScene(scene);
                primaryStage.show();
            } catch(Exception err) {
                err.printStackTrace();
            }
        });
    }
    
    public void ValidateLoginInformation()
    {

    }
    
//    public void ValidateSignupLinkClicked()
//    {
//         myController.setScreen(VultureApplication.singupviewid);
//    }
//
//    @Override
//    public void setScreenParent(ScreensController screenParent)
//    {
//        myController = screenParent;
//    }
}
