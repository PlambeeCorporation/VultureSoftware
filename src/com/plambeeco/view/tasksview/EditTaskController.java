package com.plambeeco.view.tasksview;

import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.ITechnicianModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
    private TextField txtPriority;
    @FXML
    private ListView<ITechnicianModel> lvTechnicians;

    private ITaskModel taskToEdit;

    public EditTaskController(ITaskModel taskToEdit) {
        this.taskToEdit = taskToEdit;
    }

    @FXML
    private void initialize(){
        txtJobId.setText(String.valueOf(taskToEdit.getJobId()));
        txtTaskName.setText(taskToEdit.getTaskName());
        txtareaNotes.setText(taskToEdit.getTaskNotes());
        txtHours.setText(String.valueOf(taskToEdit.getHoursNeeded()));
        txtPriority.setText(taskToEdit.getTaskPriority());
        lvTechnicians.setItems(FXCollections.observableList(taskToEdit.getAssignedTechnicians()));
    }


}
