package com.plambeeco.view.recordjobviews;

import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.models.ITaskModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TaskEditDialogViewController {
    @FXML
    private ComboBox<String> cbTasks;
    @FXML
    private TextField txtHoursNeeded;
    @FXML
    private ComboBox<String> cbTaskPriority;
    @FXML
    private TextArea txtNotes;

    private ITaskModel taskToEdit;
    private Stage dialogStage;

    public void setTaskToEdit(ITaskModel taskToEdit){
        this.taskToEdit = taskToEdit;
    }
    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public void initializeTaskToEdit(ObservableList<String> taskPriorityList){
        initializeCBTasks();
        txtHoursNeeded.setText(taskToEdit.hoursNeededProperty().getValue().toString());
        cbTaskPriority.getItems().addAll(taskPriorityList);
        cbTaskPriority.setValue(taskToEdit.getTaskPriority());
        txtNotes.setText(taskToEdit.getTaskNotes());
    }

    private void initializeCBTasks(){
        ObservableList<String> taskNames = FXCollections.observableArrayList(TaskModelProcessor.getAllTaskNames());
        cbTasks.getItems().addAll(taskNames);

        if(!cbTasks.getItems().isEmpty()){
            cbTasks.setValue(taskToEdit.getTaskName());
        }
    }

    @FXML
    private void handleOK(){
        if(isInputValid()){
            taskToEdit.setTaskName(cbTasks.getValue());
            taskToEdit.setTaskPriority(cbTaskPriority.getValue());
            taskToEdit.setHoursNeeded(Integer.valueOf(txtHoursNeeded.getText()));
            taskToEdit.setTaskNotes(txtNotes.getText());
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel(){
        dialogStage.close();
    }

    private boolean isInputValid(){
        boolean isValid = true;
        String errorMessage = "";

        if(cbTasks.getSelectionModel().getSelectedIndex() == -1){
            errorMessage += "Select a task\n";
            isValid = false;
        }
        if(txtHoursNeeded.getLength() <= 0){
            errorMessage += "Enter hours needed\n";
            isValid = false;
        }
        if(cbTaskPriority.getSelectionModel().getSelectedIndex() == -1){
            errorMessage += "Select a priority\n";
            isValid = false;
        }

        if(!isValid){
            AlertHelper.showAlert(dialogStage, "Invalid task name", errorMessage);
        }

        return isValid;
    }
}
