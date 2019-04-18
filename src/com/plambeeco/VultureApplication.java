package com.plambeeco;

import com.plambeeco.view.loginviews.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class VultureApplication extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        new LoginViewController(primaryStage);
    }

    public static void main(String[] args) {
        launch();
    }
}
