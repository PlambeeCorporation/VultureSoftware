package com.plambeeco.view.jobviews;

import com.plambeeco.dataaccess.dataprocessor.JobModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.dataaccess.repository.ITaskModelRepository;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.models.*;
import com.plambeeco.view.RootTechnicianController;
import com.plambeeco.view.tasksview.AssignTaskViewController;
import com.plambeeco.view.tasksview.EditTaskController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.stage.Window;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public class JobViewController {
    @FXML
    private TextField txtJobId;
    @FXML
    private TextField txtMotor;
    @FXML
    private TextField txtEstimatedYearOfManufacture;
    @FXML
    private DatePicker dpDateCollected;
    @FXML
    private TextField txtEstimatedLabourTime;
    @FXML
    private ComboBox<IPersonModel> cbClient;
    @FXML
    private TextField txtManufacturer;
    @FXML
    private DatePicker dpReturnDate;
    @FXML
    private ComboBox<ITechnicianModel> cbCheckingTechnician;
    @FXML
    private DatePicker dpCheckingDate;
    @FXML
    private ComboBox<ITechnicianModel> cbInspectingTechnicians;
    @FXML
    private DatePicker dpInspectionDate;
    @FXML
    private CheckBox ckbApproved;
    @FXML
    private CheckBox ckbNotApproved;

    //Job Parts Needed Table
    @FXML
    private TableView<IPartModel> tvJobParts;
    @FXML
    private TableColumn<IPartModel, String> tcPartName;
    @FXML
    private TableColumn<IPartModel, Number> tcQuantity;

    //Job Tasks Table
    @FXML
    private TableView<ITaskModel> tvJobTasks;
    @FXML
    private TableColumn<ITaskModel, String> tcTaskName;
    @FXML
    private TableColumn<ITaskModel, String> tcNotes;
    @FXML
    private TableColumn<ITaskModel, Number> tcHoursNeeded;
    @FXML
    private TableColumn<ITaskModel, String> tcPriority;
    @FXML
    private TableColumn<ITaskModel, String> tcAssignedTo;
    @FXML
    private TableColumn<ITaskModel, Boolean> tcComplete;

    private BorderPane rootScene;
    private JobModel currentJob;

    private ObservableList<ITechnicianModel> technicians = FXCollections.observableArrayList(PersonModelProcessor.getAllTechnicians());

    JobViewController(BorderPane rootScene, JobModel currentJob) {
        this.rootScene = rootScene;
        this.currentJob = currentJob;
    }

    @FXML
    private void initialize(){
        initializeMotorDetails();
        initializeTaskTable();
        initializeJobDetails();
        initializePartTable();
        initializeApproveCheckBoxes();
        initializeInspectingTechniciansCheckBox();
    }

    private void initializeMotorDetails(){
        txtMotor.setText(currentJob.getMotor().getMotorType());
        txtEstimatedYearOfManufacture.setText(String.valueOf(currentJob.getMotor().getEstimatedYearOfManufacture()));
        txtManufacturer.setText(currentJob.getMotor().getManufacturer());
    }

    private void initializeJobDetails(){
        ObservableList<IPersonModel> clients = FXCollections.observableArrayList(PersonModelProcessor.getAllClients());
        cbClient.setItems(clients);
        cbClient.setValue(currentJob.getJobDetails().getClient());

        cbCheckingTechnician.setItems(technicians);
        cbCheckingTechnician.setValue(currentJob.getJobDetails().getCheckedBy_Technician());

        txtJobId.setText(String.valueOf(currentJob.getJobId()));
        txtEstimatedLabourTime.setText(String.valueOf(currentJob.getJobDetails().getEstimatedLabourTime()));
        dpDateCollected.setValue(currentJob.getJobDetails().getDateCollected());
        dpReturnDate.setValue(currentJob.getJobDetails().getReturnDate());

        dpCheckingDate.setValue(currentJob.getJobDetails().getCheckingDate());

        if(currentJob.getInspectionDate() != null){
            dpInspectionDate.setValue(currentJob.getInspectionDate());
        }

    }

    private void initializePartTable(){
        tvJobParts.setItems(FXCollections.observableArrayList(currentJob.getPartsNeeded()));
        tcPartName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tcQuantity.setCellValueFactory(cellData -> cellData.getValue().partQuantityProperty());
    }

    private void initializeTaskTable(){
        tvJobTasks.setItems(FXCollections.observableArrayList(currentJob.getJobTasks()));
        tcTaskName.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        tcNotes.setCellValueFactory(cellData -> cellData.getValue().taskNotesProperty());
        tcHoursNeeded.setCellValueFactory(cellData -> cellData.getValue().hoursNeededProperty());
        tcPriority.setCellValueFactory(cellData -> cellData.getValue().taskPriorityProperty());
        tcAssignedTo.setCellValueFactory(cellData -> cellData.getValue().getFullNamesOfAssignedTechnicians());
        tcComplete.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isTaskCompleted()));
        tcComplete.setCellFactory(cellData -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                        item ? "Completed" : "Not Completed");
            }
        });
    }

    private void initializeInspectingTechniciansCheckBox(){
        ObservableList<ITechnicianModel> newTechnicianList = FXCollections.observableArrayList(technicians);
        ITechnicianModel dummyModel = new TechnicianModel("None",  "", "", "");
        newTechnicianList.add(0, dummyModel);
        cbInspectingTechnicians.setItems(newTechnicianList);

        if(currentJob.getInspectingTechnician() == null){
            cbInspectingTechnicians.setValue(dummyModel);
        }else{
            cbInspectingTechnicians.setValue(currentJob.getInspectingTechnician());
        }


    }

    private void initializeApproveCheckBoxes(){
        LocalDate todaysDate = LocalDate.now();

        if(todaysDate.isAfter(currentJob.getJobDetails().getReturnDate())){
            if(ckbApproved.isSelected() || ckbNotApproved.isSelected()){
                ckbApproved.isDisable();
                ckbNotApproved.isDisable();
            }
        }

        if(currentJob.isJobApproved()){
            ckbApproved.setSelected(true);
        }else{
            ckbNotApproved.setSelected(true);
        }
    }

    @FXML
    private void completeTask(){
        ITaskModel taskModel = tvJobTasks.getSelectionModel().getSelectedItem();
        if(!taskModel.isTaskCompleted()){
            taskModel.setTaskCompleted(true);
        }else{
            taskModel.setTaskCompleted(false);
        }
        initializeTaskTable();
    }

    @FXML
    private void openAssignTaskView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource("tasksview/assigntaskview.fxml"));
            AssignTaskViewController controller = new AssignTaskViewController(FXCollections.observableArrayList(currentJob.getJobTasks()));
            loader.setController(controller);
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void jobApproved(){
        ckbNotApproved.setSelected(false);
    }

    @FXML
    private void jobNotApproved(){
        ckbApproved.setSelected(false);
    }

    /**
     * Checks if the job was inspected. If it wasn't, calls normal job update query.
     * If it was inspected, checks if the inspection details are valid, and if they are
     * call update inspection details query.
     */
    @FXML
    private void confirmChanges(){
        if(wasInspected()){
            JobModel jobModel = jobWithUpdatedInspectionDetails();
            if(jobModel != null){
                JobModelProcessor.update(jobModel);
            }
        }else{
            updateJobModel();
            JobModelProcessor.update(currentJob);
        }
    }

    @FXML
    private void openPreviousWindow(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource("jobviews/alljobview.fxml"));
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);
            AllJobViewController controller = loader.getController();
            controller.setRootScene(rootScene);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void updateJobModel(){
        if(validateJobDetails()){
            //Update Job Details
            currentJob.getJobDetails().setClient(cbClient.getValue());
            currentJob.getJobDetails().setDateCollected(dpDateCollected.getValue());
            currentJob.getJobDetails().setCheckedBy_Technician(cbCheckingTechnician.getValue());
            currentJob.getJobDetails().setCheckingDate(dpCheckingDate.getValue());
            currentJob.getJobDetails().setReturnDate(dpReturnDate.getValue());

            //Update Motor Details
            currentJob.getMotor().setMotorType(txtMotor.getText());
            currentJob.getMotor().setManufacturer(txtManufacturer.getText());
            currentJob.getMotor().setEstimatedYearOfManufacture(Integer.valueOf(txtEstimatedYearOfManufacture.getText()));

            AlertHelper.showAlert(RootTechnicianController.getPrimaryStage(), "Job Details Updated!", "Job Details have been successfully updated!");
        }
    }

    private JobModel jobWithUpdatedInspectionDetails(){
        if(isInspectionValid()){
            if(validateJobDetails()){
                //Collect motor details.
                IMotorModel motorModel = new MotorModel(txtMotor.getText(),
                                                        txtManufacturer.getText(),
                                                        Integer.valueOf(txtEstimatedYearOfManufacture.getText()));
                motorModel.setMotorId(currentJob.getMotor().getMotorId());

                //Collect job details.
                IJobDetailsModel jobDetailsModel = new JobDetailsModel(cbClient.getValue(),
                                                                        cbCheckingTechnician.getValue(),
                                                                        dpCheckingDate.getValue(),
                                                                        dpDateCollected.getValue(),
                                                                        JobDetailsModel.calculateEstimatedLabourTime(tvJobTasks.getItems()),
                                                                        dpReturnDate.getValue());
                jobDetailsModel.setJobDetailsId(currentJob.getJobId());

                JobModel jobModel = new JobModel.JobBuilder()
                        .setJobId(currentJob.getJobId())
                        .setMotor(motorModel)
                        .setJobDetails(jobDetailsModel)
                        .setPartsNeeded(tvJobParts.getItems())
                        .setJobTasks(tvJobTasks.getItems())
                        .setInspectingTechnician(cbInspectingTechnicians.getValue())
                        .setInspectionDate(dpInspectionDate.getValue())
                        .setJobApproved(ckbApproved.isSelected())
                        .build();

                AlertHelper.showAlert(RootTechnicianController.getPrimaryStage(), "Job Details Updated!", "Job Details have been successfully updated!");
                return jobModel;
            }
        }

        return null;
    }

    private boolean wasInspected(){
        if(ckbApproved.isSelected() || ckbNotApproved.isSelected()){
            return true;
        }

        if(!cbInspectingTechnicians.getSelectionModel().getSelectedItem().getForename().equals("None")){
            return true;
        }

        if(dpInspectionDate.getValue() != null){
            return true;
        }

        return false;
    }

    private boolean isInspectionValid(){
        boolean inspectionValid = true;
        String errorMessage = "";

        if(!ckbApproved.isSelected() && !ckbNotApproved.isSelected()){
            errorMessage += "Click on approved or not approved checkbox.\n";
            inspectionValid = false;
        }

        if(cbInspectingTechnicians.getSelectionModel().getSelectedItem() == null){
            errorMessage += "Select inspecting technician";
            inspectionValid = false;
        }

        if(dpInspectionDate.getValue() == null){
            errorMessage += "Select inspection date";
            inspectionValid = false;
        }

        if(!inspectionValid){
            AlertHelper.showAlert(RootTechnicianController.getPrimaryStage(), "Invalid inspection details!", errorMessage);
        }
        return inspectionValid;
    }

    private boolean validateJobDetails(){
        boolean isValid = true;
        String errorMessage = "";

        if(cbClient.getValue() == null){
            isValid = false;
            errorMessage += "Select a client\n";
        }

        if(cbCheckingTechnician.getValue() == null){
            isValid = false;
            errorMessage += "Select a checking technician\n";
        }

        if(dpCheckingDate.getValue() == null){
            isValid = false;
            errorMessage += "Select a checking date\n";
        }

        if(dpDateCollected.getValue() == null){
            isValid = false;
            errorMessage += "Select a collection date\n";
        }

        if(dpReturnDate.getValue() == null){
            isValid = false;
            errorMessage += "Select a return date\n";
        }

        if(txtMotor.getText() == null || txtMotor.getLength() <= 0){
            isValid = false;
            errorMessage += "You need to enter motor name\n";
        }

        if(txtManufacturer.getText() == null || txtManufacturer.getLength() <= 0){
            isValid = false;
            errorMessage += "You need to enter motor manufacturer\n";
        }

        if(txtEstimatedYearOfManufacture.getText() == null || txtEstimatedYearOfManufacture.getLength() != 4){
            isValid = false;
            errorMessage += "You need to enter motor estimated year production\n";
        }



        if(!isValid){
            AlertHelper.showAlert(RootTechnicianController.getPrimaryStage(), "Invalid Job Details", errorMessage);
        }

        return isValid;

    }

}
