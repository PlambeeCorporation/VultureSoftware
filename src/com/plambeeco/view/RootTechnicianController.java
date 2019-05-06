package com.plambeeco.view;

import com.plambeeco.VultureApplication;
import com.plambeeco.helper.ViewHelper;
import com.plambeeco.models.IAccountModel;
import com.plambeeco.view.accountviews.EditOwnAccountViewController;
import com.plambeeco.view.jobviews.AllJobViewController;
import com.plambeeco.view.loginviews.LoginViewController;
import com.plambeeco.view.recordjobviews.RootJobRecordController;
import com.plambeeco.view.tasksview.OverdueTasksViewController;
import com.plambeeco.view.tasksview.TechnicianTaskViewController;
import com.plambeeco.view.tasksview.UnassignedTasksViewController;
import com.plambeeco.view.tasksview.UnfinishedTasksViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class RootTechnicianController {
    private BorderPane rootScene;
    private IAccountModel loggedInTechnician;

    public RootTechnicianController(IAccountModel loggedInTechnician) {
        this.loggedInTechnician = loggedInTechnician;
        VultureApplication.getPrimaryStage().setTitle(loggedInTechnician.getAccountOwner().getFullName().get());
        initRootLayout();
    }

    @FXML
    private void initRootLayout()
    {
        try {
            //Load record job layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource(ViewHelper.ROOT_TECHNICIAN_VIEW_RESOURCE));
            loader.setController(this);
            rootScene = loader.load();

            //Show the scene
            Scene scene = new Scene(rootScene);
            VultureApplication.getPrimaryStage().setScene(scene);
            VultureApplication.getPrimaryStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openEditOwnAccountView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource(ViewHelper.EDIT_OWN_ACCOUNT_VIEW_RESOURCE));
            EditOwnAccountViewController technicianTaskViewController = new EditOwnAccountViewController(loggedInTechnician);
            loader.setController(technicianTaskViewController);
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void openTechnicianTasksView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource(ViewHelper.TECHNICIANS_TASK_VIEW_RESOURCE));
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
            UnassignedTasksViewController unassignedTasksViewController = new UnassignedTasksViewController(rootScene);
            loader.setController(unassignedTasksViewController);
            AnchorPane stage = loader.load();
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
        VultureApplication.getPrimaryStage().setTitle("Login");
        new LoginViewController();
    }
}
