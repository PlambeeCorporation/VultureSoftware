package com.plambeeco.view.tasksview;

import com.plambeeco.dataaccess.dataprocessor.JobModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.ITechnicianModel;
import com.plambeeco.models.JobModel;
import com.plambeeco.view.RootTechnicianController;
import com.plambeeco.view.jobviews.JobViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private TableColumn<ITaskModel, Number> tcJobId;
    @FXML
    private TableColumn<ITaskModel, String> tcTaskName;
    @FXML
    private TableColumn<ITaskModel, String> tcNotes;
    @FXML
    private TableColumn<ITaskModel, Number> tcHours;
    @FXML
    private TableColumn<ITaskModel, String> tcPriority;
    @FXML
    private TableColumn<ITaskModel, String> tcAssignedTechnicians;

    //Technician components
    @FXML
    private ComboBox<String> cbSortTechnicianBy;
    @FXML
    private TableView<ITechnicianModel> tvTechnicians;
    @FXML
    private TableColumn<ITechnicianModel, String> tcTechnicianName;
    @FXML
    private TableColumn<ITechnicianModel, String> tcTaskPreference;
    @FXML
    private TableColumn<ITechnicianModel, String> tcTasksCurrentlyAssigned;
    private ObservableList<ITechnicianModel> technicians =
            FXCollections.observableArrayList(PersonModelProcessor.getAllTechnicians());
    private JobModel currentJob;
    private BorderPane rootScene;
    private ITaskModel selectedTask;
    private List<ITaskModel> tasksToRemove = new ArrayList<>();
    private Map<ITechnicianModel, Integer> techniciansToRemove = new HashMap<>();

    public AssignTaskViewController(JobModel currentJob, BorderPane rootScene) {
        this.currentJob = currentJob;
        this.rootScene = rootScene;
    }

    @FXML
    private void initialize(){

        initializeTaskTableView();
        initializeTechnicianTableView();
    }

    private void initializeTaskTableView(){
        tvAvailableTasks.setItems(FXCollections.observableArrayList(currentJob.getJobTasks()));
        tcJobId.setCellValueFactory(cellData -> cellData.getValue().jobIdProperty());
        tcTaskName.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        tcNotes.setCellValueFactory(cellData -> cellData.getValue().taskNotesProperty());
        tcHours.setCellValueFactory(cellData -> cellData.getValue().hoursNeededProperty());
        tcPriority.setCellValueFactory(cellData -> cellData.getValue().taskPriorityProperty());
        tcAssignedTechnicians.setCellValueFactory(cellData -> cellData.getValue().getFullNamesOfAssignedTechnicians());
    }

    private void initializeTechnicianTableView(){
        tvTechnicians.setItems(technicians);
        tcTechnicianName.setCellValueFactory(cellData -> cellData.getValue().getFullName());
        tcTasksCurrentlyAssigned.setCellValueFactory(cellData -> cellData.getValue().getNamesOfCurrentlyAssignedTasks());
    }

    @FXML
    private void editTask(){
        ITaskModel selectedTask = tvAvailableTasks.getSelectionModel().getSelectedItem();

        if(selectedTask != null){
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(RootTechnicianController.class.getResource("tasksview/edittaskview.fxml"));
                EditTaskController controller = new EditTaskController(selectedTask, techniciansToRemove);
                loader.setController(controller);
                AnchorPane editTaskView = loader.load();

                Stage taskEditView = new Stage();
                taskEditView.setTitle("Edit Task");
                taskEditView.initModality(Modality.WINDOW_MODAL);
                taskEditView.initOwner(RootTechnicianController.getPrimaryStage());
                controller.setEditTaskStage(taskEditView);

                Scene scene = new Scene(editTaskView);
                taskEditView.setScene(scene);
                taskEditView.showAndWait();
                initializeTaskTableView();
                initializeTechnicianTableView();
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            AlertHelper.showAlert(RootTechnicianController.getPrimaryStage(), "Task not selected", "Select a task!");
        }
    }

    @FXML
    private void removeTask(){
        ITaskModel selectedTask = tvAvailableTasks.getSelectionModel().getSelectedItem();
        if(selectedTask.getTaskId() > 0){
            tasksToRemove.add(selectedTask);
        }
        tvAvailableTasks.getItems().remove(selectedTask);
    }

    @FXML
    private void assignTechnician(){
        ITechnicianModel technicianModel = tvTechnicians.getSelectionModel().getSelectedItem();
        if(technicianModel != null && selectedTask != null){
            if(selectedTask.getAssignedTechnicians().size() > 0){
                for (ITechnicianModel technician : selectedTask.getAssignedTechnicians()) {
                    if (technician.getPersonId() != technicianModel.getPersonId()) {
                        selectedTask.addTechnician(technicianModel);
                        technicianModel.getCurrentTasks().add(selectedTask);
                        initializeTaskTableView();
                        initializeTechnicianTableView();
                    } else {
                        AlertHelper.showAlert(RootTechnicianController.getPrimaryStage(), "Technician already assigned!",
                                technicianModel.getFullName().get() + " has been already assigned to this task");
                    }
                }
            }else{
                selectedTask.addTechnician(technicianModel);
                technicianModel.getCurrentTasks().add(selectedTask);
                initializeTaskTableView();
                initializeTechnicianTableView();
            }
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

    @FXML
    private void confirm(){
        removeTasksFromDatabase();
        updateTasksInDatabase();

        AlertHelper.showAlert(RootTechnicianController.getPrimaryStage(), "Updating Tasks successfull", "Tasks have been updated!");
    }

    private void removeTasksFromDatabase(){
        tasksToRemove.forEach(task -> {
            TaskModelProcessor.removeJobTask(task.getJobId(), task.getTaskId());
            task.getAssignedTechnicians().forEach(technician ->
                    TaskModelProcessor.removeTaskAssignedTechnician(task.getTaskId(), technician.getPersonId()));
        });

        techniciansToRemove.keySet().forEach(technician ->
                TaskModelProcessor.removeTaskAssignedTechnician(techniciansToRemove.get(technician), technician.getPersonId()));
    }

    private void updateTasksInDatabase(){
        tvAvailableTasks.getItems().forEach(task -> {
            TaskModelProcessor.update(task);
            task.getAssignedTechnicians().forEach(technician -> TaskModelProcessor.addTaskAssignedTechnician(task, technician.getPersonId()));
        });
    }

    @FXML
    private void back(){
        JobModel job = JobModelProcessor.getById(currentJob.getJobId());
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource("jobviews/jobview.fxml"));
            JobViewController controller = new JobViewController(rootScene, job);
            loader.setController(controller);
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);


        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
