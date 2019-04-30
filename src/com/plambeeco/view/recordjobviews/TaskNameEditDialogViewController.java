package com.plambeeco.view.recordjobviews;

import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.helper.AlertHelper;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TaskNameEditDialogViewController {
    @FXML
    private TextField txtPartName;
    private ObservableSet<String> taskNames;

    private Stage dialogStage;
    private String oldTaskName;

    /**
     * Current that name that will be updated.
     * @param oldTaskName current that name that will be updated.
     */
    public void setOldTaskName(String oldTaskName){
        this.oldTaskName = oldTaskName;
    }

    /**
     * Sets reference to its self.
     * @param dialogStage reference to its self.
     */
    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    /**
     * Sets list of all currents task names.
     * @param taskNames list of all current task names.
     */
    public void setTaskNames(ObservableSet<String> taskNames){
        this.taskNames = taskNames;
    }

    /**
     * When OK button is pressed, checks if the input data is valid, then asks
     * ListOfPartNames class to create new part with the name written in the
     * txtPartName TextField.
     */
    @FXML
    private void handleOK(){
        if(isInputValid()){
            String taskName = txtPartName.getText();

            if(oldTaskName == null){
                if(!taskNames.add(taskName)){
                    AlertHelper.showAlert(dialogStage, "Duplicate task name", "A task with this name already exists.");
                }else{
                    TaskModelProcessor.addTaskName(taskName);
                }
            }else{
                TaskModelProcessor.updateTaskName(oldTaskName, taskName);
                taskNames.remove(oldTaskName);
                taskNames.add(taskName);
            }


            dialogStage.close();
        }
    }

    /**
     * Closes the dialogStage.
     */
    @FXML
    private void handleCancel(){
        dialogStage.close();
    }

    /**
     * Validates the input. If the txtPartName TextField is empty,
     * show Alert.
     * @return  true if input is valid, false otherwise.
     */
    private boolean isInputValid(){
        if(txtPartName.getText() == null || txtPartName.getLength() <= 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid task name");
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText("Enter valid task name");
            alert.showAndWait();

            return false;
        }

        return true;
    }
}
