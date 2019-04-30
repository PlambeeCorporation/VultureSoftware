package com.plambeeco.view.tasksview;

import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.models.IAccountModel;
import com.plambeeco.models.ITaskModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
}
