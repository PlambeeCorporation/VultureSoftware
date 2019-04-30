package com.plambeeco.view;

import com.plambeeco.models.IAccountModel;
import com.plambeeco.view.jobviews.AllJobViewController;
import com.plambeeco.view.loginviews.LoginViewController;
import com.plambeeco.view.recordjobviews.RootJobRecordController;
import com.plambeeco.view.tasksview.TechnicianTaskViewController;
import com.plambeeco.view.tasksview.UnassignedTasksViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RootTechnicianController {
    private static Stage primaryStage;
    private BorderPane rootScene;
    private IAccountModel loggedInTechnician;

    public RootTechnicianController(Stage primaryStage, IAccountModel loggedInTechnician) {
        this.primaryStage = primaryStage;
        this.loggedInTechnician = loggedInTechnician;
        primaryStage.setTitle(loggedInTechnician.getAccountOwner().getFullName().get());
        initRootLayout();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @FXML
    private void initRootLayout()
    {
        try {
            //Load record job layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource("roottechnicianview.fxml"));
            loader.setController(this);
            rootScene = loader.load();

            //Show the scene
            Scene scene = new Scene(rootScene);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openTechnicianTasksView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource("tasksview/techniciantasksview.fxml"));
            TechnicianTaskViewController technicianTaskViewController = new TechnicianTaskViewController(loggedInTechnician);
            loader.setController(technicianTaskViewController);
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void openRecordNewJobView(){
        new RootJobRecordController(rootScene);
    }

    @FXML
    private void openUnassignedTasksView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource("tasksview/unassignedtasksview.fxml"));
            AnchorPane stage = loader.load();
            UnassignedTasksViewController unassignedTasksViewController = loader.getController();
            unassignedTasksViewController.setRootScene(rootScene);
            rootScene.setCenter(stage);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void openAllJobsView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource("jobviews/alljobview.fxml"));
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);
            AllJobViewController controller = loader.getController();
            controller.setRootScene(rootScene);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void logOut(){
        new LoginViewController(primaryStage);
    }
}
