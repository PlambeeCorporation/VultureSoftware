package com.plambeeco.view.tasksview;

import com.plambeeco.VultureApplication;
import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.helper.ConstantValuesHelper;
import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.ITechnicianModel;
import com.plambeeco.view.recordjobviews.RecordPartsNeededController;
import com.plambeeco.view.recordjobviews.TaskNameEditDialogViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class EditTaskViewController {
    @FXML
    private TextField txtJobId;
    @FXML
    private ComboBox<String> cbTaskNames;
    @FXML
    private TextArea txtareaNotes;
    @FXML
    private TextField txtHours;
    @FXML
    private ComboBox<String> cbTaskPriority;
    @FXML
    private ListView<ITechnicianModel> lvTechnicians;

    private ITaskModel taskToEdit;

    private Stage editTaskStage;
    private Map<ITechnicianModel, Integer> techniciansToRemove;

    public void setEditTaskStage(Stage editTaskStage) {
        this.editTaskStage = editTaskStage;
    }

    public EditTaskViewController(ITaskModel taskToEdit, Map<ITechnicianModel, Integer> techniciansToRemove) {
        this.taskToEdit = taskToEdit;
        this.techniciansToRemove = techniciansToRemove;
    }

    @FXML
    private void initialize(){
        txtJobId.setText(String.valueOf(taskToEdit.getJobId()));
        txtareaNotes.setText(taskToEdit.getTaskNotes());
        txtHours.setText(String.valueOf(taskToEdit.getHoursNeeded()));
        initializeComboBox();
        initializeCBTaskPriority();
        initializeListBox();
    }

    private void initializeComboBox(){
        ObservableList<String> sortedTaskNames = FXCollections.observableArrayList(TaskModelProcessor.getAllTaskNames());
        sortedTaskNames.sort(String::compareTo);
        sortedTaskNames.add(0, ConstantValuesHelper.ADD_NEW_TASK_NAME);
        cbTaskNames.setItems(sortedTaskNames);
        cbTaskNames.setValue(taskToEdit.getTaskName());
    }

    private void initializeListBox(){
        lvTechnicians.setItems(FXCollections.observableList(taskToEdit.getAssignedTechnicians()));
    }

    private void initializeCBTaskPriority(){
        ObservableList<String> taskPriority = FXCollections.observableArrayList();
        taskPriority.add("Low");
        taskPriority.add("Medium");
        taskPriority.add("High");
        taskPriority.add("Very High");
        cbTaskPriority.getItems().addAll(taskPriority);
        cbTaskPriority.setValue(taskToEdit.getTaskPriority());
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
                initializeComboBox();
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }

    @FXML
    private void removeTechnician(){
        ITechnicianModel selectedTechnician = lvTechnicians.getSelectionModel().getSelectedItem();
        techniciansToRemove.put(selectedTechnician, taskToEdit.getTaskId());
        selectedTechnician.getCurrentTasks().remove(taskToEdit);
        taskToEdit.getAssignedTechnicians().remove(selectedTechnician);
        initializeListBox();
    }

    @FXML
    private void handleOK(){
        if(validateTaskDetails()){
            taskToEdit.setTaskName(cbTaskNames.getValue());
            taskToEdit.setTaskNotes(txtareaNotes.getText());
            taskToEdit.setHoursNeeded(Integer.valueOf(txtHours.getText()));
            taskToEdit.setTaskPriority(cbTaskPriority.getValue());
            taskToEdit.setAssignedTechnicians(lvTechnicians.getItems());
            editTaskStage.close();
        }
    }

    @FXML
    private void handleCancel(){
        editTaskStage.close();
    }

    private boolean validateTaskDetails(){
        //TODO - Validate task details
        return true;
    }
}
