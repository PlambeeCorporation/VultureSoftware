package com.plambeeco.view.recordjobviews;

import com.plambeeco.dataaccess.dataprocessor.*;
import com.plambeeco.models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RootJobRecordController {
    private Stage primaryStage;
    private BorderPane rootScene;
    @FXML
    private BorderPane rootJobScene;
    @FXML
    private BorderPane borderPaneInsideTopBorderPane;

    private RecordPartsNeededController recordPartsNeededController;
    private RecordMotorDetailsViewController recordMotorDetailsViewController;
    private RecordTasksDetailsViewController recordTasksDetailsViewController;
    private RecordJobDetailsViewController recordJobDetailsViewController;

    public RootJobRecordController(Stage primaryStage, BorderPane rootScene) {
        this.primaryStage = primaryStage;
        this.rootScene = rootScene;
        primaryStage.setTitle("Record new Job");

        initRootLayout();
        initRecordDetailsView();
        initMotorModelView();
        initPartsNeededView();
        initTaskView();
    }

    private void initRootLayout() {
        try{
            //Load record job layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootJobRecordController.class.getResource("rootjobrecordview.fxml"));
            loader.setController(this);
            rootJobScene = loader.load();

            rootScene.setCenter(rootJobScene);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void initMotorModelView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootJobRecordController.class.getResource("recordmotordetailsview.fxml"));
            AnchorPane motorModelView = loader.load();

            borderPaneInsideTopBorderPane.setRight(motorModelView);

            recordMotorDetailsViewController = loader.getController();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void initPartsNeededView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootJobRecordController.class.getResource("recordpartsneededview.fxml"));
            AnchorPane partsNeededView = loader.load();

            rootJobScene.setRight(partsNeededView);

            recordPartsNeededController = loader.getController();
            recordPartsNeededController.setRootScene(primaryStage);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void initTaskView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootJobRecordController.class.getResource("recordtaskdetailsview.fxml"));
            AnchorPane taskView = loader.load();

            rootJobScene.setLeft(taskView);

            recordTasksDetailsViewController = loader.getController();
            recordTasksDetailsViewController.setRootScene(primaryStage);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void initRecordDetailsView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootJobRecordController.class.getResource("recordjobdetailsview.fxml"));
            AnchorPane jobView = loader.load();

            borderPaneInsideTopBorderPane.setLeft(jobView);

            recordJobDetailsViewController = loader.getController();
            recordJobDetailsViewController.setrootJobRecordController(this);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void createJob(){
        IMotorModel motor = recordMotorDetailsViewController.createMotorModel();
        List<IPartModel> partsNeeded = recordPartsNeededController.getPartsNeeded();
        List<ITaskModel> tasksNeeded = recordTasksDetailsViewController.getTasksNeeded();
        IJobDetailsModel jobDetails = recordJobDetailsViewController.getJobDetails();
        jobDetails.setEstimatedLabourTime(getEstimatedLabourTime(tasksNeeded));

        JobModel job = new JobModel.JobBuilder()
                .setMotor(motor)
                .setJobDetails(jobDetails)
                .setPartsNeeded(partsNeeded)
                .setJobTasks(tasksNeeded)
                .setInspectingTechnician(null)
                .setInspectionDate(null)
                .setJobApproved(false)
                .build();

        addJobToDatabase(job);
    }

    private int getEstimatedLabourTime(List<ITaskModel> tasksNeeded){
        int estimatedLabourTime = 0;

        for(ITaskModel taskModel : tasksNeeded){
            estimatedLabourTime += taskModel.getHoursNeeded();
        }

        return estimatedLabourTime;
    }

    private void addJobToDatabase(JobModel job){
        MotorModelProcessor.add(job.getMotor());
        JobDetailsModelProcessor.add(job.getJobDetails());
        JobModelProcessor.add(job);
        PartModelProcessor.addAll(job.getJobId(), job.getPartsNeeded());
        TaskModelProcessor.addAll(job.getJobId(), job.getJobTasks());
    }
}
