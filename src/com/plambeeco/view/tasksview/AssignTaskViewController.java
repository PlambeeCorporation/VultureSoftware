package com.plambeeco.view.tasksview;

import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.ITechnicianModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AssignTaskViewController {
    //TODO - Add sorting, editing and removing functionality.
    @FXML
    private TextField txtSelectedTask;

    //Task components
    @FXML
    private ComboBox<String> cbSortTaskBy;
    @FXML
    private TableView<ITaskModel> tvAvailableTasks;
    @FXML
    private TableColumn<ITaskModel, Number> tbJobId;
    @FXML
    private TableColumn<ITaskModel, String> tbTaskName;
    @FXML
    private TableColumn<ITaskModel, String> tbNotes;
    @FXML
    private TableColumn<ITaskModel, Number> tbHours;
    @FXML
    private TableColumn<ITaskModel, String> tbPriority;
    @FXML
    private TableColumn<ITaskModel, String> tbAssignedTechnicians;

    //Technician components
    @FXML
    private ComboBox<String> cbSortTechnicianBy;
    @FXML
    private TableView<ITechnicianModel> tvTechnicians;
    @FXML
    private TableColumn<ITechnicianModel, String> tbTechnicianName;
    @FXML
    private TableColumn<ITechnicianModel, String> tbTaskPreference;
    @FXML
    private TableColumn<ITechnicianModel, String> tbTasksCurrentlyAssigned;

    private ObservableList<ITaskModel> tasks;
    private ITaskModel selectedTask;

    public AssignTaskViewController(ObservableList<ITaskModel> tasks) {
        this.tasks = tasks;
    }

    @FXML
    private void initialize(){
        initializeTaskTableView();
        initializeTechnicianTableView();
        initializeSortByTaskComboBox();
        initializeSortByTechnicianComboBox();
    }

    private void initializeTaskTableView(){

        tvAvailableTasks.setItems(tasks);
        tbJobId.setCellValueFactory(cellData -> cellData.getValue().jobIdProperty());
        tbTaskName.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        tbNotes.setCellValueFactory(cellData -> cellData.getValue().taskNotesProperty());
        tbHours.setCellValueFactory(cellData -> cellData.getValue().hoursNeededProperty());
        tbPriority.setCellValueFactory(cellData -> cellData.getValue().taskPriorityProperty());
        tbAssignedTechnicians.setCellValueFactory(cellData -> cellData.getValue().getFullNamesOfAssignedTechnicians());
    }

    private void initializeTechnicianTableView(){
        ObservableList<ITechnicianModel> technicians =
                FXCollections.observableArrayList(PersonModelProcessor.getAllTechnicians());

        tvTechnicians.setItems(technicians);
        tbTechnicianName.setCellValueFactory(cellData -> cellData.getValue().getFullName());
        tbTasksCurrentlyAssigned.setCellValueFactory(cellData -> cellData.getValue().getNamesOfCurrentlyAssignedTasks());
    }

    private void initializeSortByTaskComboBox(){
        ObservableList<String> sortByTaskOptions = FXCollections.observableArrayList();
        sortByTaskOptions.add("Job Id");
        sortByTaskOptions.add("Task Name");
        sortByTaskOptions.add("Hours Needed");
        sortByTaskOptions.add("Priority");
        sortByTaskOptions.add("Assigned Technicians");
        cbSortTaskBy.getItems().addAll(sortByTaskOptions);
    }

    private void initializeSortByTechnicianComboBox(){
        ObservableList<String> sortByTechnicianOptions = FXCollections.observableArrayList();
        sortByTechnicianOptions.add("Technician Name");
        sortByTechnicianOptions.add("Task Preference");
        sortByTechnicianOptions.add("Number of tasks currently assigned");
        cbSortTechnicianBy.getItems().addAll(sortByTechnicianOptions);
    }

    @FXML
    private void editTask(){
        ITaskModel selectedTask = tvAvailableTasks.getSelectionModel().getSelectedItem();
        if(selectedTask != null){
            //createTaskEditDialogView(selectedTask);
        }
    }

    @FXML
    private void removeTask(){

    }

    @FXML
    private void assignTechnician(){
        ITechnicianModel technicianModel = tvTechnicians.getSelectionModel().getSelectedItem();


        if(technicianModel != null && selectedTask != null){
            selectedTask.addTechnician(technicianModel);
            TaskModelProcessor.addTaskAssignedTechnician(selectedTask, technicianModel.getPersonId());
            initializeTaskTableView();
        }
    }

    @FXML
    private void selectTask(){
        ITaskModel taskModel = tvAvailableTasks.getSelectionModel().getSelectedItem();

        if(taskModel != null){
            selectedTask = taskModel;
            txtSelectedTask.setText(selectedTask.getTaskName());
        }
    }
}
