package com.plambeeco.view.recordjobviews;

import com.plambeeco.VultureApplication;
import com.plambeeco.dataaccess.dataprocessor.*;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

public class RootJobRecordViewController {
    private BorderPane rootScene;
    @FXML
    private BorderPane rootJobScene;
    @FXML
    private BorderPane borderPaneInsideTopBorderPane;

    private RecordPartsNeededController recordPartsNeededController;
    private RecordMotorDetailsViewController recordMotorDetailsViewController;
    private RecordTasksDetailsViewController recordTasksDetailsViewController;
    private RecordJobDetailsViewController recordJobDetailsViewController;

    public RootJobRecordViewController(BorderPane rootScene) {
        this.rootScene = rootScene;
        VultureApplication.getPrimaryStage().setTitle("Record new Job");

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
            loader.setLocation(RootJobRecordViewController.class.getResource("rootjobrecordview.fxml"));
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
            loader.setLocation(RootJobRecordViewController.class.getResource("recordmotordetailsview.fxml"));
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
            loader.setLocation(RootJobRecordViewController.class.getResource("recordpartsneededview.fxml"));
            AnchorPane partsNeededView = loader.load();

            rootJobScene.setRight(partsNeededView);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void initTaskView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootJobRecordViewController.class.getResource("recordtaskdetailsview.fxml"));
            AnchorPane taskView = loader.load();

            rootJobScene.setLeft(taskView);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void initRecordDetailsView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootJobRecordViewController.class.getResource("recordjobdetailsview.fxml"));
            AnchorPane jobView = loader.load();

            borderPaneInsideTopBorderPane.setLeft(jobView);

            recordJobDetailsViewController = loader.getController();
            recordJobDetailsViewController.setrootJobRecordController();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void createJob(){
        IMotorModel motor = recordMotorDetailsViewController.createMotorModel();
        List<IPartModel> partsNeeded = recordPartsNeededController.getPartsNeeded();
        List<ITaskModel> tasksNeeded = recordTasksDetailsViewController.getTasksNeeded();
        IJobDetailsModel jobDetails = recordJobDetailsViewController.createJobDetails();
        jobDetails.setEstimatedLabourTime(JobDetailsModel.calculateEstimatedLabourTime(tasksNeeded));

        if(motor != null && jobDetails != null){
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
            AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Job Created", "Job Created successfully!");
        }
    }

    private void addJobToDatabase(JobModel job){
        MotorModelProcessor.add(job.getMotor());
        JobDetailsModelProcessor.add(job.getJobDetails());
        JobModelProcessor.add(job);
        PartModelProcessor.addAll(job.getJobId(), job.getPartsNeeded());
        TaskModelProcessor.addAll(job.getJobId(), job.getJobTasks());
    }
}
