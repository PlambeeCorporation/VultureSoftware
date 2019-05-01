package com.plambeeco.view.tasksview;

import com.plambeeco.dataaccess.dataprocessor.JobModelProcessor;
import com.plambeeco.helper.ViewHelper;
import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.JobModel;
import com.plambeeco.view.RootTechnicianController;
import com.plambeeco.view.jobviews.JobViewController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class OverdueOrUnfinishedTasksViewController {
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

    private BorderPane rootScene;
    private ObservableList<ITaskModel> tasks;

    public OverdueOrUnfinishedTasksViewController(List<ITaskModel> tasks, BorderPane rootScene) {
        this.tasks = FXCollections.observableArrayList(tasks);
        this.rootScene = rootScene;
    }

    @FXML
    private void initialize(){
        tvTasks.setItems(tasks);
        tcJobId.setCellValueFactory(cellData -> cellData.getValue().jobIdProperty());
        tcTaskName.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        tcTaskPriority.setCellValueFactory(cellData -> cellData.getValue().taskPriorityProperty());
        tcTaskNotes.setCellValueFactory(cellData -> cellData.getValue().taskNotesProperty());
        tcHoursNeeded.setCellValueFactory(cellData -> cellData.getValue().hoursNeededProperty());
        tcDeadline.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTaskFinishDate()));
    }

    @FXML
    private void openJobView(){
        JobModel job = JobModelProcessor.getById(tvTasks.getSelectionModel().getSelectedItem().getJobId());

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource(ViewHelper.JOB_VIEW_RESOURCE));
            JobViewController controller = new JobViewController(rootScene, job);
            loader.setController(controller);
            ViewHelper.getViewsResourcesStack().push(ViewHelper.OVERDUE_OR_UNFINISHED_TASKS_VIEW_RESOURCE);
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);


        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
