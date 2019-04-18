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

        //new ScreensFramework(primaryStage);
    }

    private static Stage primaryStage;
//
//    // Instantiates a string equal to the name of a file
//    public static String loginviewid = "LoginView";
//    // Instantiates a string equal to the name of a file with it's extension
//    public static String loginviewfile = "view/loginviews/loginview.fxml";
//
//    // Instantiates a string equal to the name of a file
//    public static String singupviewid = "SignupView";
//    // Instantiates a string equal to the name of a file with it's extension
//    public static String signupviewfile = "view/loginviews/SignupView.fxml";
//
//    public void start(Stage primaryStage) throws IOException
//    {
//        try {
//            AnchorPane fxmlLoader = (AnchorPane) FXMLLoader.load(getClass().getResource(loginviewfile));
//            Scene scene = new Scene(fxmlLoader);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void resizeScreen()
    {
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch();
    }
}
