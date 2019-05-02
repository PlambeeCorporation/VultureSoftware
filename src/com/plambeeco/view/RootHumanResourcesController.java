package com.plambeeco.view;

import com.plambeeco.helper.ViewHelper;
import com.plambeeco.models.IAccountModel;
import com.plambeeco.view.jobviews.AllJobViewController;
import com.plambeeco.view.loginviews.LoginViewController;
import com.plambeeco.view.tasksview.OverdueTasksViewController;
import com.plambeeco.view.tasksview.UnassignedTasksViewController;
import com.plambeeco.view.tasksview.UnfinishedTasksViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RootHumanResourcesController {
    private static Stage primaryStage;
    private BorderPane rootScene;
    private IAccountModel loggedInAccount;

    public RootHumanResourcesController(Stage primaryStage, IAccountModel loggedInAccount) {
        this.primaryStage = primaryStage;
        this.loggedInAccount = loggedInAccount;
        primaryStage.setTitle(loggedInAccount.getAccountOwner().getFullName().get());
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
            loader.setLocation(RootHumanResourcesController.class.getResource(ViewHelper.ROOT_HUMAN_RESOURCES_VIEW_RESOURCE));
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
    private void openCreateNewAccountView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource(ViewHelper.CREATE_NEW_ACCOUNT_VIEW_RESOURCE));
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void openAllJobsView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource(ViewHelper.ALL_JOBS_VIEW_RESOURCE));
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);
            AllJobViewController controller = loader.getController();
            controller.setRootScene(rootScene);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void openOverdueTasksView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource(ViewHelper.OVERDUE_TASKS_VIEW_RESOURCE));
            OverdueTasksViewController overdueTasksViewController = new OverdueTasksViewController(rootScene);
            loader.setController(overdueTasksViewController);
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void openUnassignedTasksView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource(ViewHelper.UNASSIGNED_TASKS_VIEW_RESOURCE));
            AnchorPane stage = loader.load();
            UnassignedTasksViewController unassignedTasksViewController = loader.getController();
            unassignedTasksViewController.setRootScene(rootScene);
            rootScene.setCenter(stage);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void openUnfinishedTasksView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource(ViewHelper.UNFINISHED_TASKS_VIEW_RESOURCE));
            UnfinishedTasksViewController unfinishedTasksViewController = new UnfinishedTasksViewController(rootScene);
            loader.setController(unfinishedTasksViewController);
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void logOut(){
        new LoginViewController(primaryStage);
    }
}
