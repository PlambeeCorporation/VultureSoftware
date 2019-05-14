package com.plambeeco.view.recordjobviews;

import com.plambeeco.VultureApplication;
import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.helper.ConstantValuesHelper;
import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.TaskModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RecordTasksDetailsViewController {
    @FXML
    private ComboBox<String> cbTaskNames;
    @FXML
    private TextField txtHoursNeeded;
    @FXML
    private ComboBox<String> cbTaskPriority;
    @FXML
    private TextArea txtNotes;
    @FXML
    private TableView<ITaskModel> tvTasksToDo;
    @FXML
    private TableColumn<ITaskModel, String> tbTask;
    @FXML
    private TableColumn<ITaskModel, String> tbNotes;
    @FXML
    private TableColumn<ITaskModel, Number> tbHours;
    @FXML
    private TableColumn<ITaskModel, String> tbPriority;

    private ObservableList<ITaskModel> tasksNeeded = FXCollections.observableArrayList();

    public List<ITaskModel> getTasksNeeded(){
        return tasksNeeded;
    }

    /**
     * Initializes all form components.
     */
    @FXML
    private void initialize(){
        initializeCBTaskNames();
        initializeTableViewAndColumns();
        cbTaskPriority.setItems(ConstantValuesHelper.TASK_PRIORITIES);
    }

    /**
     * Initializes combo box containing task names.
     */
    private void initializeCBTaskNames(){
        if(!cbTaskNames.getItems().isEmpty()){
            cbTaskNames.getItems().clear();
        }

        cbTaskNames.getItems().addAll(getSortedTaskNames());
    }

    /**
     * Receives all task names from the database and sorts them in a list.
     * @return Sorted list of all task names.
     */
    private ObservableList<String> getSortedTaskNames(){
        ObservableList<String> sortedTaskNames = FXCollections.observableArrayList(TaskModelProcessor.getAllTaskNames());
        sortedTaskNames.sort(String::compareTo);
        sortedTaskNames.add(0, ConstantValuesHelper.ADD_NEW_TASK_NAME);
        return sortedTaskNames;
    }

    /**
     * Initializes table view and columns.
     */
    private void initializeTableViewAndColumns(){
        tvTasksToDo.setItems(tasksNeeded);
        tbTask.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        tbNotes.setCellValueFactory(cellData -> cellData.getValue().taskNotesProperty());
        tbHours.setCellValueFactory(cellData -> cellData.getValue().hoursNeededProperty());
        tbPriority.setCellValueFactory(cellData -> cellData.getValue().taskPriorityProperty());
    }

    @FXML
    private void createNewTaskName(){
        if(cbTaskNames.getValue().equals(ConstantValuesHelper.ADD_NEW_TASK_NAME)){
            createTaskNameEditDialogView(ConstantValuesHelper.ADD_NEW_TASK_NAME, null);
            initializeCBTaskNames();
            cbTaskNames.getSelectionModel().selectLast();
        }
    }

    @FXML
    private void editTaskName(){
        String taskName = cbTaskNames.getValue();
        int index = cbTaskNames.getSelectionModel().getSelectedIndex();
        if(!taskName.equals(ConstantValuesHelper.ADD_NEW_TASK_NAME)){
            createTaskNameEditDialogView("Edit Part", taskName);
            initializeCBTaskNames();
            cbTaskNames.getSelectionModel().select(index);
        }
    }

    /**
     * Creates a new task for the job.
     */
    @FXML
    private void createTask(){
        if(validateTaskDetails()){
            String taskName = cbTaskNames.getValue();
            String taskPriority = cbTaskPriority.getValue();
            String taskNotes = txtNotes.getText();
            int hoursNeeded = Integer.valueOf(txtHoursNeeded.getText());
            tasksNeeded.add(new TaskModel(taskName, taskPriority, taskNotes, hoursNeeded));
        }
    }

    /**
     * Edits selected task.
     */
    @FXML
    private void editTask(){
        ITaskModel selectedTask = tvTasksToDo.getSelectionModel().getSelectedItem();
        if(selectedTask != null){
            createTaskEditDialogView(selectedTask);
        }
    }

    /**
     * Removes selected task.
     */
    @FXML
    private void removeTask(){
        ITaskModel selectedTask = tvTasksToDo.getSelectionModel().getSelectedItem();
        tasksNeeded.remove(selectedTask);
        initializeTableViewAndColumns();
    }

    /**
     * Creates a new scene, in which the user can edit the task details.
     * @param selectedPart  Task to edit.
     */
    private void createTaskEditDialogView(ITaskModel selectedPart){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RecordTasksDetailsViewController.class.getResource("taskeditdialogview.fxml"));
            AnchorPane taskEditDialogView = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Task");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(VultureApplication.getPrimaryStage());

            Scene scene = new Scene(taskEditDialogView);
            dialogStage.setScene(scene);

            TaskEditDialogViewController taskEditDialogViewController = loader.getController();
            taskEditDialogViewController.setTaskToEdit(selectedPart);
            taskEditDialogViewController.setDialogStage(dialogStage);
            taskEditDialogViewController.initializeTaskToEdit();


            dialogStage.showAndWait();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void createTaskNameEditDialogView(String title, String taskName){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RecordPartsNeededController.class.getResource("tasknameeditdialogview.fxml"));
            AnchorPane taskEditDialogView = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(VultureApplication.getPrimaryStage());

            Scene scene = new Scene(taskEditDialogView);
            dialogStage.setScene(scene);

            TaskNameEditDialogViewController taskNameEditDialogViewController = loader.getController();
            taskNameEditDialogViewController.setDialogStage(dialogStage);
            taskNameEditDialogViewController.setOldTaskName(taskName);
            ObservableSet<String> taskNames = FXCollections.observableSet();
            taskNames.addAll(cbTaskNames.getItems());
            taskNameEditDialogViewController.setTaskNames(taskNames);

            dialogStage.showAndWait();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private boolean validateTaskDetails(){
        boolean isValid = true;
        String errorMessage = "";

        if(cbTaskNames.getValue() == null){
            isValid = false;
            errorMessage += "Select a task name!\n";
        }

        if(cbTaskPriority.getValue() == null){
            isValid = false;
            errorMessage += "Select a priority!\n";
        }

        if(txtHoursNeeded.getLength() < 1){
            isValid = false;
            errorMessage += "Enter hours needed!\n";
        }

        if(Integer.valueOf(txtHoursNeeded.getText()) > 48){
            isValid = false;
            errorMessage += "Surely, it will take you less than 48 hours to complete the task.\n";
        }

        for (ITaskModel task: tasksNeeded) {
            if(cbTaskNames.getValue() != null &&
                    cbTaskNames.getValue().equals(task.getTaskName())){
                isValid = false;
                errorMessage = cbTaskNames.getValue() + " has been already added!\n";
                break;
            }
        }

        if(!isValid){
            AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Incorrrect Task Details", errorMessage);
        }

        return isValid;
    }
}
