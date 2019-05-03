package com.plambeeco.view.tasksview;

import com.plambeeco.VultureApplication;
import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.helper.ConstantValuesHelper;
import com.plambeeco.helper.TextFieldHelper;
import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.JobModel;
import com.plambeeco.models.TaskModel;
import com.plambeeco.view.recordjobviews.RecordPartsNeededController;
import com.plambeeco.view.recordjobviews.TaskNameEditDialogViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AddNewTaskViewController {
    @FXML
    private ComboBox<String> cbTaskNames;
    @FXML
    private TextArea txtaNotes;
    @FXML
    private TextField txtHours;
    @FXML
    private ComboBox<String> cbPriority;

    private Stage addTaskStage;
    private JobModel currentJob;

    AddNewTaskViewController(JobModel currentJob) {
        this.currentJob = currentJob;
    }

    void setAddTaskStage(Stage addTaskStage) {
        this.addTaskStage = addTaskStage;
    }

    @FXML
    private void initialize(){
        initializeComboBoxes();
        TextFieldHelper.allowNumbersOnly(txtHours);
        cbPriority.setItems(ConstantValuesHelper.TASK_PRIORITIES);
    }

    private void initializeComboBoxes(){
        ObservableList<String> sortedTaskNames = FXCollections.observableArrayList(TaskModelProcessor.getAllTaskNames());
        sortedTaskNames.sort(String::compareTo);
        sortedTaskNames.add(0, ConstantValuesHelper.ADD_NEW_TASK_NAME);
        cbTaskNames.setItems(sortedTaskNames);
    }

    @FXML
    private void addNewTaskName(){
        if(cbTaskNames.getValue().equals(ConstantValuesHelper.ADD_NEW_TASK_NAME)){
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(RecordPartsNeededController.class.getResource("tasknameeditdialogview.fxml"));
                AnchorPane taskEditDialogView = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Add New Task Name");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(VultureApplication.getPrimaryStage());

                Scene scene = new Scene(taskEditDialogView);
                dialogStage.setScene(scene);

                TaskNameEditDialogViewController taskNameEditDialogViewController = loader.getController();
                taskNameEditDialogViewController.setDialogStage(dialogStage);
                ObservableSet<String> taskNames = FXCollections.observableSet();
                taskNames.addAll(cbTaskNames.getItems());
                taskNameEditDialogViewController.setTaskNames(taskNames);

                dialogStage.showAndWait();
                initializeComboBoxes();
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }

    @FXML
    private void handleOK(){
        String taskName = cbTaskNames.getValue();
        String taskNotes = txtaNotes.getText();
        int hours = Integer.valueOf(txtHours.getText());
        String taskPriority = cbPriority.getValue();
        ITaskModel taskModel = new TaskModel(taskName,taskPriority, taskNotes, hours);
        taskModel.setJobId(currentJob.getJobId());
        currentJob.getJobTasks().add(taskModel);
        addTaskStage.close();
    }

    @FXML
    private void handleCancel(){
        addTaskStage.close();
    }
}
