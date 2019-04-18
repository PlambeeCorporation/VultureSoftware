package com.plambeeco.view;

import com.plambeeco.view.assigningtasksview.AssignTaskViewController;
import com.plambeeco.view.jobviews.AllJobViewController;
import com.plambeeco.view.recordjobviews.RootJobRecordController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootTechnicianController implements Initializable {
    private Stage primaryStage;
    private BorderPane rootScene;

    public RootTechnicianController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Technician view");
        initRootLayout();
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
    private void openRecordNewJobView(){

        new RootJobRecordController(primaryStage, rootScene);
    }

    @FXML
    private void openUnassignedTasksView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource("assigningtasksview/assigntaskview.fxml"));
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);

        }catch(IOException e){
            e.printStackTrace();
        }
    }
//
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
