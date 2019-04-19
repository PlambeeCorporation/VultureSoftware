package com.plambeeco.view.jobviews;

import com.plambeeco.dataaccess.repository.ITaskModelRepository;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.models.IPartModel;
import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.ITechnicianModel;
import com.plambeeco.models.JobModel;
import com.plambeeco.view.RootTechnicianController;
import com.plambeeco.view.tasksview.AssignTaskViewController;
import com.plambeeco.view.tasksview.EditTaskController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


public class JobViewController {
    @FXML
    private TextField txtJobId;
    @FXML
    private TextField txtMotor;
    @FXML
    private TextField txtEstimatedYearOfManufacture;
    @FXML
    private DatePicker dpDateCollected;
    @FXML
    private TextField txtEstimatedLabourTime;
    @FXML
    private TextField txtClient;
    @FXML
    private TextField txtManufacturer;
    @FXML
    private DatePicker dpReturnDate;
    @FXML
    private TextField txtCheckedBy;
    @FXML
    private DatePicker dpCheckingDate;
    @FXML
    private ComboBox<ITechnicianModel> cbInspectingTechnicians;
    @FXML
    private DatePicker dpInspectionDate;
    @FXML
    private CheckBox ckbApproved;
    @FXML
    private CheckBox ckbNotApproved;

    //Job Parts Needed Table
    @FXML
    private TableView<IPartModel> tvJobParts;
    @FXML
    private TableColumn<IPartModel, String> tcPartName;
    @FXML
    private TableColumn<IPartModel, Number> tcQuantity;

    //Job Tasks Table
    @FXML
    private TableView<ITaskModel> tvJobTasks;
    @FXML
    private TableColumn<ITaskModel, String> tcTaskName;
    @FXML
    private TableColumn<ITaskModel, String> tcNotes;
    @FXML
    private TableColumn<ITaskModel, Number> tcHoursNeeded;
    @FXML
    private TableColumn<ITaskModel, String> tcAssignedTo;
    @FXML
    private TableColumn<ITaskModel, Boolean> tcComplete;

    private BorderPane rootScene;
    private JobModel currentJob;

    JobViewController(BorderPane rootScene, JobModel currentJob) {
        this.rootScene = rootScene;
        this.currentJob = currentJob;
    }

    @FXML
    private void initialize(){
        initializeMotorDetails();
        initializeTaskTable();
        initializeJobDetails();
        initializePartTable();
    }

    private void initializeMotorDetails(){
        txtMotor.setText(currentJob.getMotor().getMotorType());
        txtEstimatedYearOfManufacture.setText(String.valueOf(currentJob.getMotor().getEstimatedYearOfManufacture()));
        txtManufacturer.setText(currentJob.getMotor().getManufacturer());
    }

    private void initializeJobDetails(){
        txtJobId.setText(String.valueOf(currentJob.getJobId()));
        txtClient.setText(currentJob.getJobDetails().getClient().getFullName().get());
        txtEstimatedLabourTime.setText(String.valueOf(currentJob.getJobDetails().getEstimatedLabourTime()));
        dpDateCollected.setValue(currentJob.getJobDetails().getDateCollected());
        dpReturnDate.setValue(currentJob.getJobDetails().getReturnDate());
        txtCheckedBy.setText(currentJob.getJobDetails().getCheckedBy_Technician().getFullName().get());
        dpCheckingDate.setValue(currentJob.getJobDetails().getCheckingDate());
    }

    private void initializePartTable(){
        tvJobParts.setItems(FXCollections.observableArrayList(currentJob.getPartsNeeded()));
        tcPartName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tcQuantity.setCellValueFactory(cellData -> cellData.getValue().partQuantityProperty());
    }

    private void initializeTaskTable(){
        tvJobTasks.setItems(FXCollections.observableArrayList(currentJob.getJobTasks()));
        tcTaskName.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        tcNotes.setCellValueFactory(cellData -> cellData.getValue().taskNotesProperty());
        tcHoursNeeded.setCellValueFactory(cellData -> cellData.getValue().hoursNeededProperty());
        tcAssignedTo.setCellValueFactory(cellData -> cellData.getValue().getFullNamesOfAssignedTechnicians());
        tcComplete.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isTaskCompleted()));
    }

    @FXML
    private void completeTask(){
        ITaskModel taskModel = tvJobTasks.getSelectionModel().getSelectedItem();
        if(!taskModel.isTaskCompleted()){
            taskModel.setTaskCompleted(true);
        }else{
            taskModel.setTaskCompleted(false);
        }
        initializeTaskTable();
    }

    @FXML
    private void openAssignTasksToTechniciansView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource("tasksview/assigntaskview.fxml"));
            AssignTaskViewController controller = new AssignTaskViewController(FXCollections.observableArrayList(currentJob.getJobTasks()));
            loader.setController(controller);
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void openEditTaskView(){
        ITaskModel taskModel = tvJobTasks.getSelectionModel().getSelectedItem();

        if(taskModel != null){
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(RootTechnicianController.class.getResource("tasksview/edittask.fxml"));
                EditTaskController controller = new EditTaskController(taskModel);
                loader.setController(controller);
                AnchorPane stage = loader.load();
                rootScene.setCenter(stage);

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
