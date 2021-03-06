package com.plambeeco.view.tasksview;

import com.plambeeco.VultureApplication;
import com.plambeeco.dataaccess.dataprocessor.JobModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.models.IAccountModel;
import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.JobModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class TechnicianTaskViewController {
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
    private TableColumn<ITaskModel, String> tcAssignedTechnicians;
    @FXML
    private TableColumn<ITaskModel, Boolean> tcTaskCompleted;
    @FXML
    private TableColumn<ITaskModel, LocalDate> tcDeadline;

    private IAccountModel loggedInTechnician;

    public TechnicianTaskViewController(IAccountModel loggedInTechnician) {
        this.loggedInTechnician = loggedInTechnician;
    }

    @FXML
    private void initialize(){
        initializeTable();
    }

    private void initializeTable(){
        ObservableList<ITaskModel> tasks = FXCollections.observableArrayList(
                TaskModelProcessor.getTechniciansCurrentlyAssignedTasks(loggedInTechnician.getAccountOwner().getPersonId()));
        for (ITaskModel task : tasks) {
            task.setAssignedTechnicians(PersonModelProcessor.getAssignedTechnicians(task.getTaskId()));
        }

        tvTasks.setItems(tasks);
        tcJobId.setCellValueFactory(cellData -> cellData.getValue().jobIdProperty());
        tcTaskName.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        tcTaskPriority.setCellValueFactory(cellData -> cellData.getValue().taskPriorityProperty());
        tcTaskNotes.setCellValueFactory(cellData -> cellData.getValue().taskNotesProperty());
        tcHoursNeeded.setCellValueFactory(cellData -> cellData.getValue().hoursNeededProperty());
        tcAssignedTechnicians.setCellValueFactory(cellData -> cellData.getValue().getFullNamesOfAssignedTechnicians());
        tcTaskCompleted.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isTaskCompleted()));
        tcTaskCompleted.setCellFactory(cellData -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                        item ? "Completed" : "Not Completed");
            }
        });
        tcDeadline.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTaskFinishDate()));
    }

    @FXML
    private void completeTask(){
        ITaskModel taskModel = tvTasks.getSelectionModel().getSelectedItem();
        JobModel job = JobModelProcessor.getById(taskModel.getJobId());

        if(!job.isJobApproved()){
            if(!taskModel.isTaskCompleted()){
                taskModel.setTaskCompleted(true);
            }else{
                taskModel.setTaskCompleted(false);
            }
            TaskModelProcessor.update(taskModel);
            initializeTable();
        }else{
            AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Cannot change the task completion", "The job for this task is marked as approved");
        }
    }
}
