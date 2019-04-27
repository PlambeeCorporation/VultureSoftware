package com.plambeeco.view.tasksview;

import com.plambeeco.dataaccess.dataprocessor.JobDetailsModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.JobModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.models.*;
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

import java.io.IOException;
import java.util.*;

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
    private List<ITaskModel> tasksToUpdate = new ArrayList<>();

    public AssignTaskViewController(JobModel currentJob, BorderPane rootScene) {
        this.currentJob = currentJob;
        this.rootScene = rootScene;
        tasksToUpdate.addAll(currentJob.getJobTasks());
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
        //Work around javafx being retarded. Used for refreshing tcAssignedTechnicians column.
        tvAvailableTasks.getColumns().get(5).setVisible(false);
        tvAvailableTasks.getColumns().get(5).setVisible(true);
    }

    private void initializeTechnicianTableView(){
        tvTechnicians.setItems(technicians);
        tcTechnicianName.setCellValueFactory(cellData -> cellData.getValue().getFullName());
        tcTasksCurrentlyAssigned.setCellValueFactory(cellData -> cellData.getValue().getNamesOfCurrentlyAssignedTasks());
        //Work around javafx being retarded. Used for refreshing tcTasksCurrentlyAssigned column.
        tvTechnicians.getColumns().get(1).setVisible(false);
        tvTechnicians.getColumns().get(1).setVisible(true);
    }

    @FXML
    private void addTask(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource("tasksview/addnewtaskview.fxml"));
            AddNewTaskViewController controller = new AddNewTaskViewController(currentJob);
            loader.setController(controller);
            AnchorPane editTaskView = loader.load();

            Stage taskAddView = new Stage();
            taskAddView.setTitle("Add Task");
            taskAddView.initModality(Modality.WINDOW_MODAL);
            taskAddView.initOwner(RootTechnicianController.getPrimaryStage());
            controller.setAddTaskStage(taskAddView);

            Scene scene = new Scene(editTaskView);
            taskAddView.setScene(scene);
            taskAddView.showAndWait();
            initializeTaskTableView();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void editTask(){
        ITaskModel selectedTask = tvAvailableTasks.getSelectionModel().getSelectedItem();

        if(selectedTask != null){
            try{
                Map<ITechnicianModel, Integer> removeMap = new HashMap<>();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(RootTechnicianController.class.getResource("tasksview/edittaskview.fxml"));
                EditTaskViewController controller = new EditTaskViewController(selectedTask, removeMap);
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
                techniciansToRemove.putAll(removeMap);
                //This loops is used to update the assigned tasks in technicians table view.
                for (ITechnicianModel technician : technicians) {
                    for (ITechnicianModel technicianToRemove : removeMap.keySet()) {
                        if (technician.getPersonId() == technicianToRemove.getPersonId()) {
                            technicians.remove(technician);
                            technicians.add(technicianToRemove);
                        }
                    }
                }
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

        tasksToUpdate.removeIf(taskModel -> taskModel.equals(selectedTask));

        tvAvailableTasks.getItems().remove(selectedTask);
        technicians.forEach(technician ->
                technician.getCurrentTasks().removeIf(taskModel -> taskModel.equals(selectedTask)));
        initializeTechnicianTableView();
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
        removeAssignedTechniciansFromDatabase();
        updateTasksInDatabase();
        addNewTasksToDatabase();
        updateJobDetails();
        AlertHelper.showAlert(RootTechnicianController.getPrimaryStage(), "Updating Tasks successfull", "Tasks have been updated!");
    }

    private void removeTasksFromDatabase(){
        tasksToRemove.forEach(task -> {
            TaskModelProcessor.removeJobTask(task.getJobId(), task.getTaskId());
            task.getAssignedTechnicians().forEach(technician ->
                    TaskModelProcessor.removeTaskAssignedTechnician(task.getTaskId(), technician.getPersonId()));
        });
    }

    private void removeAssignedTechniciansFromDatabase(){
        techniciansToRemove.keySet().forEach(technician ->
                TaskModelProcessor.removeTaskAssignedTechnician(techniciansToRemove.get(technician), technician.getPersonId()));
    }

    private void updateTasksInDatabase(){
        tasksToUpdate.forEach(task -> {
            TaskModelProcessor.update(task);
            task.getAssignedTechnicians().forEach(technician ->
                    TaskModelProcessor.addTaskAssignedTechnician(task, technician.getPersonId()));
        });
    }

    private void updateJobDetails(){
        currentJob.getJobDetails().setEstimatedLabourTime(JobDetailsModel.calculateEstimatedLabourTime(tvAvailableTasks.getItems()));
        JobDetailsModelProcessor.update(currentJob.getJobDetails());
    }

    private void addNewTasksToDatabase(){
       List<ITaskModel> tasksToAdd = new ArrayList<>();

        tvAvailableTasks.getItems().forEach(task ->{
            if(tasksToUpdate.size() > 0){
                tasksToUpdate.forEach(taskToUpdate -> {
                    if(task.getTaskId() != taskToUpdate.getTaskId()) {
                        tasksToAdd.add(task);
                    }
                });
            }else {
                tasksToAdd.addAll(tvAvailableTasks.getItems());
            }
        });

        TaskModelProcessor.addAll(currentJob.getJobId(), tasksToAdd);
        tasksToAdd.forEach(task ->
                task.getAssignedTechnicians().forEach(technician ->
                    TaskModelProcessor.addTaskAssignedTechnician(task, technician.getPersonId())));


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
