package com.plambeeco.view.tasksview;

import com.plambeeco.VultureApplication;
import com.plambeeco.dataaccess.dataprocessor.JobModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.helper.ViewHelper;
import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.JobModel;
import com.plambeeco.view.RootHumanResourcesViewController;
import com.plambeeco.view.RootTechnicianViewController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;

import static com.plambeeco.models.AccountModel.AccountType;


public class UnassignedTasksViewController {
    @FXML
    private TableView<ITaskModel> tvTasks;
    @FXML
    private TableColumn<ITaskModel, Number> tcJobId;
    @FXML
    private TableColumn<ITaskModel, String> tcTaskName;
    @FXML
    private TableColumn<ITaskModel, String> tcTaskPriority;
    @FXML
    private TableColumn<ITaskModel, String> tcTaskNotes;
    @FXML
    private TableColumn<ITaskModel, Number> tcHoursNeeded;
    @FXML
    private TableColumn<ITaskModel, LocalDate> tcDeadline;
    @FXML
    private Button btnAssignTasks;

    private BorderPane rootScene;

    public UnassignedTasksViewController(BorderPane rootScene) {
        this.rootScene = rootScene;
    }

    @FXML
    private void initialize(){
        tvTasks.setItems(FXCollections.observableList(FXCollections.observableArrayList(TaskModelProcessor.getAllUnassignedTasks())));
        tcJobId.setCellValueFactory(cellData -> cellData.getValue().jobIdProperty());
        tcTaskName.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        tcTaskPriority.setCellValueFactory(cellData -> cellData.getValue().taskPriorityProperty());
        tcTaskNotes.setCellValueFactory(cellData -> cellData.getValue().taskNotesProperty());
        tcHoursNeeded.setCellValueFactory(cellData -> cellData.getValue().hoursNeededProperty());
        tcDeadline.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTaskFinishDate()));
        disableButton();
    }

    private void disableButton(){
        try{
            AccountType accountType = RootHumanResourcesViewController.getLoggedInAccountType();
            if(accountType == AccountType.HumanResources){
                btnAssignTasks.setVisible(false);
            }
        }catch(NullPointerException e){
            //This is just so it runs the view if accounttype is null.
            //Sadly in java if(accountType == null) doesn't work, so this is work around using
            //try catch.
        }
    }

    @FXML
    private void loadJob(){
        if(tvTasks.getSelectionModel().getSelectedItem() == null){
            AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Job not selected", "Select a job!");
            return;
        }

        JobModel job = JobModelProcessor.getById(tvTasks.getSelectionModel().getSelectedItem().getJobId());

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianViewController.class.getResource(ViewHelper.ASSIGN_TASKS_VIEW_RESOURCE));
            AssignTaskViewController controller = new AssignTaskViewController(job, rootScene);
            loader.setController(controller);
            ViewHelper.getViewsResourcesStack().push(ViewHelper.UNASSIGNED_TASKS_VIEW_RESOURCE);
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);


        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
