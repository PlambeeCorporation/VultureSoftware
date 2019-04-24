package com.plambeeco.view.tasksview;

import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.ITechnicianModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class EditTaskController {
    @FXML
    private TextField txtJobId;
    @FXML
    private TextField txtTaskName;
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

    public EditTaskController(ITaskModel taskToEdit, Map<ITechnicianModel, Integer> techniciansToRemove) {
        this.taskToEdit = taskToEdit;
        this.techniciansToRemove = techniciansToRemove;
    }

    @FXML
    private void initialize(){
        txtJobId.setText(String.valueOf(taskToEdit.getJobId()));
        txtTaskName.setText(taskToEdit.getTaskName());
        txtareaNotes.setText(taskToEdit.getTaskNotes());
        txtHours.setText(String.valueOf(taskToEdit.getHoursNeeded()));
        initializeCBTaskPriority();
        intializeListBox();
    }

    private void intializeListBox(){
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
    private void removeTechnician(){
        ITechnicianModel selectedTechnician = lvTechnicians.getSelectionModel().getSelectedItem();
        techniciansToRemove.put(selectedTechnician, taskToEdit.getTaskId());
        taskToEdit.getAssignedTechnicians().remove(selectedTechnician);
        intializeListBox();
    }

    @FXML
    private void handleOK(){
        if(validateTaskDetails()){
            taskToEdit.setTaskName(txtTaskName.getText());
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
