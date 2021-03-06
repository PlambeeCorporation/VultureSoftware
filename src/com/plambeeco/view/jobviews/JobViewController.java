package com.plambeeco.view.jobviews;

import com.plambeeco.VultureApplication;
import com.plambeeco.dataaccess.dataprocessor.JobModelProcessor;
import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.helper.ViewHelper;
import com.plambeeco.models.*;
import com.plambeeco.view.RootHumanResourcesViewController;
import com.plambeeco.view.RootTechnicianViewController;
import com.plambeeco.view.jobviews.partsview.PartDetailsViewController;
import com.plambeeco.view.tasksview.AssignTaskViewController;
import com.plambeeco.view.tasksview.OverdueTasksViewController;
import com.plambeeco.view.tasksview.UnfinishedTasksViewController;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.time.LocalDate;

import static com.plambeeco.models.AccountModel.AccountType;


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

    //Buttons
    @FXML
    private Button btnAddPart;
    @FXML
    private Button btnEditPart;
    @FXML
    private Button btnRemovePart;
    @FXML
    private Button btnOpenAssignTasksView;
    @FXML
    private Button btnTaskCompleted;
    @FXML
    private Button btnConfirm;

    private BorderPane rootScene;
    private JobModel currentJob;

    private ObservableList<ITechnicianModel> technicians = FXCollections.observableArrayList(PersonModelProcessor.getAllTechnicians());

    public JobViewController(BorderPane rootScene, JobModel currentJob) {
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
        initializeEditability();
    }



    /**
     * Initializes motor details/
     */
    private void initializeMotorDetails(){
        txtMotor.setText(currentJob.getMotor().getMotorType());
        txtEstimatedYearOfManufacture.setText(String.valueOf(currentJob.getMotor().getEstimatedYearOfManufacture()));
        txtManufacturer.setText(currentJob.getMotor().getManufacturer());
    }

    /**
     * Initializes core job details.
     */
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

    /**
     * Initializes part details in a table view.
     */
    private void initializePartTable(){
        tvJobParts.setItems(FXCollections.observableArrayList(currentJob.getPartsNeeded()));
        tcPartName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tcQuantity.setCellValueFactory(cellData -> cellData.getValue().partQuantityProperty());
    }

    /**
     * Initializes task details in a table view.
     */
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

    /**
     * Initializes inspecting technicians check box.
     */
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

    /**
     * Initializes job approved checkbox.
     */
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

    private void initializeEditability(){
        try{
            AccountType accountType = RootHumanResourcesViewController.getLoggedInAccountType();
            if(accountType == AccountType.HumanResources){
                disableComponents();
                ckbApproved.setDisable(true);
                ckbNotApproved.setDisable(true);
                btnConfirm.setDisable(true);
            }
        }catch(NullPointerException e){
            if(currentJob.isJobApproved()){
                disableComponents();
            }else{
                enableComponents();
            }
        }
    }

    private void disableComponents(){
        txtJobId.setDisable(true);
        txtMotor.setDisable(true);
        txtEstimatedYearOfManufacture.setDisable(true);
        dpDateCollected.setDisable(true);
        txtEstimatedLabourTime.setDisable(true);
        cbClient.setDisable(true);
        txtManufacturer.setDisable(true);
        dpReturnDate.setDisable(true);
        cbCheckingTechnician.setDisable(true);
        dpCheckingDate.setDisable(true);
        cbInspectingTechnicians.setDisable(true);
        dpInspectionDate.setDisable(true);
        btnAddPart.setDisable(true);
        btnEditPart.setDisable(true);
        btnRemovePart.setDisable(true);
        btnOpenAssignTasksView.setDisable(true);
        btnTaskCompleted.setDisable(true);
    }

    private void enableComponents(){
        txtJobId.setDisable(false);
        txtMotor.setDisable(false);
        txtEstimatedYearOfManufacture.setDisable(false);
        dpDateCollected.setDisable(false);
        txtEstimatedLabourTime.setDisable(false);
        cbClient.setDisable(false);
        txtManufacturer.setDisable(false);
        dpReturnDate.setDisable(false);
        cbCheckingTechnician.setDisable(false);
        dpCheckingDate.setDisable(false);
        cbInspectingTechnicians.setDisable(false);
        dpInspectionDate.setDisable(false);
        btnAddPart.setDisable(false);
        btnEditPart.setDisable(false);
        btnRemovePart.setDisable(false);
        btnOpenAssignTasksView.setDisable(false);
        btnTaskCompleted.setDisable(false);txtJobId.setDisable(false);
        txtMotor.setDisable(false);
        txtEstimatedYearOfManufacture.setDisable(false);
        dpDateCollected.setDisable(false);
        txtEstimatedLabourTime.setDisable(false);
        cbClient.setDisable(false);
        txtManufacturer.setDisable(false);
        dpReturnDate.setDisable(false);
        cbCheckingTechnician.setDisable(false);
        dpCheckingDate.setDisable(false);
        cbInspectingTechnicians.setDisable(false);
        dpInspectionDate.setDisable(false);
        btnAddPart.setDisable(false);
        btnEditPart.setDisable(false);
        btnRemovePart.setDisable(false);
        btnOpenAssignTasksView.setDisable(false);
        btnTaskCompleted.setDisable(false);
    }

    /**
     * Sets the task as complete or not complete.
     */
    @FXML
    private void completeTask(){
        ITaskModel taskModel = tvJobTasks.getSelectionModel().getSelectedItem();
        if(!taskModel.isTaskCompleted()){
            taskModel.setTaskCompleted(true);
        }else{
            taskModel.setTaskCompleted(false);
            ckbApproved.setSelected(false);
            ckbNotApproved.setSelected(true);
        }
        initializeTaskTable();
    }

    /**
     * Opens assign task view.
     */
    @FXML
    private void openAssignTaskView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianViewController.class.getResource(ViewHelper.ASSIGN_TASKS_VIEW_RESOURCE));
            AssignTaskViewController controller = new AssignTaskViewController(currentJob, rootScene);
            loader.setController(controller);
            ViewHelper.getViewsResourcesStack().push(ViewHelper.JOB_VIEW_RESOURCE);
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);

        }catch(IOException e){
            e.getMessage();
            e.printStackTrace();
        }
    }

    @FXML
    private void addPart(){
        openPartOrEditView("Add new Part", null);
    }

    @FXML
    private void editPart(){
        IPartModel partModel = tvJobParts.getSelectionModel().getSelectedItem();
        openPartOrEditView("Edit Part", partModel);
    }

    /**
     * Opens view that allows to add new part or edit current one.
     * @param sceneTitle name of the view.
     * @param partModel part model to edit.
     */
    private void openPartOrEditView(String sceneTitle, IPartModel partModel){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JobViewController.class.getResource("partsview/addoreditpartdetailsview.fxml"));

            Stage partStage = new Stage();
            partStage.setTitle(sceneTitle);
            partStage.initModality(Modality.WINDOW_MODAL);
            partStage.initOwner(VultureApplication.getPrimaryStage());

            PartDetailsViewController partDetailsViewController =
                    new PartDetailsViewController(partStage, partModel, currentJob);
            loader.setController(partDetailsViewController);
            AnchorPane partDialogStage = loader.load();

            Scene scene = new Scene(partDialogStage);
            partStage.setScene(scene);


            partStage.showAndWait();
            initializePartTable();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Removes the part from the table.
     */
    @FXML
    private void removePart(){
        IPartModel part = tvJobParts.getSelectionModel().getSelectedItem();
        currentJob.getPartsNeeded().remove(part);
        initializePartTable();
    }

    /**
     * Approves the job.
     */
    @FXML
    private void jobApproved(){
        for (ITaskModel task : currentJob.getJobTasks()) {
            if (!task.isTaskCompleted()) {
                ckbApproved.setSelected(false);
                AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Not all tasks are completed",
                        "Ensure all tasks are marked as complete, before approving the inspection!");

                return;
            }
        }
        ckbNotApproved.setSelected(false);
    }

    /**
     * Disapproves the job.
     */
    @FXML
    private void jobNotApproved(){
        ckbApproved.setSelected(false);
    }

    /**
     * Confirms the changes made in the form if the input validation is valid.
     */
    @FXML
    private void confirmChanges(){
        if(wasInspected()){
            JobModel jobModel = jobWithUpdatedInspectionDetails();
            if(jobModel != null){
                JobModelProcessor.update(jobModel);
                currentJob = jobModel;
            }
        }else{
            updateJobModel();
            JobModelProcessor.update(currentJob);
        }

        initializeEditability();
    }

    /**
     * Opens previous view.
     */
    @FXML
    private void openPreviousWindow(){
        try{
            FXMLLoader loader = new FXMLLoader();
            String previousScreen = ViewHelper.getViewsResourcesStack().pop();
            loader.setLocation(RootTechnicianViewController.class.getResource(previousScreen));
            AnchorPane stage = null;
            switch (previousScreen) {
                case ViewHelper.ALL_JOBS_VIEW_RESOURCE:
                    stage = loader.load();
                    AllJobViewController controller = loader.getController();
                    controller.setRootScene(rootScene);
                    break;
                case ViewHelper.UNFINISHED_TASKS_VIEW_RESOURCE:
                    UnfinishedTasksViewController unfinishedTasksViewController = new UnfinishedTasksViewController(rootScene);
                    loader.setController(unfinishedTasksViewController);
                    stage = loader.load();
                    break;
                case ViewHelper.OVERDUE_TASKS_VIEW_RESOURCE:
                    OverdueTasksViewController overdueTasksViewController = new OverdueTasksViewController(rootScene);
                    loader.setController(overdueTasksViewController);
                    stage = loader.load();
                    break;
            }


            rootScene.setCenter(stage);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates current job model. This method is called if no attempt for inspection was made.
     */
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


            AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Job Details Updated!", "Job Details have been successfully updated!");
        }
    }

    /**
     * Updates current job model with the inspection details.
     * @return Job model with inspection details.
     */
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

                AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Job Details Updated!", "Job Details have been successfully updated!");
                return jobModel;
            }
        }

        return null;
    }

    /**
     * Checks if an attempt to add inspection details was made.
     * @return True if any of inspection details fields have a value. False otherwise.
     */
    private boolean wasInspected(){
        if(ckbApproved.isSelected()){
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

    /**
     * Checks if the inspection details are valid.
     * @return True if the inspection details are valid. False otherwise.
     */
    private boolean isInspectionValid(){
        boolean inspectionValid = true;
        String errorMessage = "";

        if(cbInspectingTechnicians.getSelectionModel().getSelectedItem() == null ||
                cbInspectingTechnicians.getSelectionModel().getSelectedItem().getForename().equals("None")){
            errorMessage += "Select inspecting technician\n";
            inspectionValid = false;
        }

        if(dpInspectionDate.getValue() == null){
            errorMessage += "Select inspection date";
            inspectionValid = false;
        }

        if(!inspectionValid){
            AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Invalid inspection details!", errorMessage);
        }
        return inspectionValid;
    }

    /**
     * Validates job details input.
     * @return True if valid, false otherwise.
     */
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
            AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Invalid Job Details", errorMessage);
        }

        return isValid;

    }

}
