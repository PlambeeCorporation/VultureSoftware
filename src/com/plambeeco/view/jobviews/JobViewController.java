package com.plambeeco.view.jobviews;

import com.plambeeco.dataaccess.repository.ITaskModelRepository;
import com.plambeeco.models.IPartModel;
import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.ITechnicianModel;
import com.plambeeco.models.JobModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;


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
    private TextField txtClient;
    @FXML
    private TextField txtManufacturer;
    @FXML
    private DatePicker dpReturnDate;
    @FXML
    private TextField txtCheckedBy;
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
    private TableColumn<ITaskModel, String> tcAssignedTo;
    @FXML
    private TableColumn<ITaskModel, Boolean> tcComplete;

    private JobModel currentJob;

    void setCurrentJob(JobModel currentJob) {
        this.currentJob = currentJob;
    }

    void initializeView(){
        txtJobId.setText(String.valueOf(currentJob.getJobId()));
        //Motor details
        txtMotor.setText(currentJob.getMotor().getMotorType());
        txtEstimatedYearOfManufacture.setText(String.valueOf(currentJob.getMotor().getEstimatedYearOfManufacture()));
        txtManufacturer.setText(currentJob.getMotor().getManufacturer());
        //Job Details
        txtClient.setText(currentJob.getJobDetails().getClient().getFullName().get());
        txtEstimatedLabourTime.setText(String.valueOf(currentJob.getJobDetails().getEstimatedLabourTime()));
        dpDateCollected.setValue(currentJob.getJobDetails().getDateCollected());
        dpReturnDate.setValue(currentJob.getJobDetails().getReturnDate());
        txtCheckedBy.setText(currentJob.getJobDetails().getCheckedBy_Technician().getFullName().get());
        dpCheckingDate.setValue(currentJob.getJobDetails().getCheckingDate());
        //Part Details
        tvJobParts.setItems(FXCollections.observableArrayList(currentJob.getPartsNeeded()));
        tcPartName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tcQuantity.setCellValueFactory(cellData -> cellData.getValue().partQuantityProperty());
        //Task Details
        tvJobTasks.setItems(FXCollections.observableArrayList(currentJob.getJobTasks()));
        tcTaskName.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        tcNotes.setCellValueFactory(cellData -> cellData.getValue().taskNotesProperty());
        tcHoursNeeded.setCellValueFactory(cellData -> cellData.getValue().hoursNeededProperty());
        tcAssignedTo.setCellValueFactory(cellData -> cellData.getValue().getFullNamesOfAssignedTechnicians());
        tcComplete.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isTaskCompleted()));
    }
}
