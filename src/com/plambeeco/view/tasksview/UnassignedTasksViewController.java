package com.plambeeco.view.tasksview;

import com.plambeeco.dataaccess.dataprocessor.JobModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.JobModel;
import com.plambeeco.view.RootTechnicianController;
import com.plambeeco.view.jobviews.JobViewController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

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

    private BorderPane rootScene;

    public void setRootScene(BorderPane rootScene) {
        this.rootScene = rootScene;
    }

    public UnassignedTasksViewController() {
    }

    @FXML
    private void initialize(){
        tvTasks.setItems(FXCollections.observableList(TaskModelProcessor.getAllUnassignedTasks()));
        tcJobId.setCellValueFactory(cellData -> cellData.getValue().jobIdProperty());
        tcTaskName.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        tcTaskPriority.setCellValueFactory(cellData -> cellData.getValue().taskPriorityProperty());
        tcTaskNotes.setCellValueFactory(cellData -> cellData.getValue().taskNotesProperty());
        tcHoursNeeded.setCellValueFactory(cellData -> cellData.getValue().hoursNeededProperty());
    }

    @FXML
    private void loadJob(){
        JobModel job = JobModelProcessor.getById(tvTasks.getSelectionModel().getSelectedItem().getJobId());

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource("jobviews/jobview.fxml"));
            JobViewController controller = new JobViewController(rootScene, job);
            loader.setController(controller);
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);


        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
