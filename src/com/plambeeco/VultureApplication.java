package com.plambeeco;

import com.plambeeco.view.loginviews.LoginViewController;
import javafx.application.Application;
import javafx.stage.Stage;

public class VultureApplication extends Application {

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        new LoginViewController();
    }

    public static void main(String[] args) {
        launch();
    }
}