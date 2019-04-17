package com.plambeeco.view.jobviews;

import com.plambeeco.dataaccess.dataprocessor.JobModelProcessor;
import com.plambeeco.models.JobModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class AllJobViewController {
    @FXML
    TableView<JobModel> tvJobs;
    @FXML
    TableColumn<JobModel, Number> tbJobId;
    @FXML
    TableColumn<JobModel, String> tbClient;
    @FXML
    TableColumn<JobModel, Number> tbEstimatedLabourTime;
    @FXML
    TableColumn<JobModel, LocalDate> tbCollectedDate;
    @FXML
    TableColumn<JobModel, LocalDate> tbReturnDate;
    @FXML
    TableColumn<JobModel, Boolean> tbJobApproved;

    private AnchorPane stage;

    public void setStage(AnchorPane stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize(){
        ObservableList<JobModel> jobs = FXCollections.observableArrayList(JobModelProcessor.getAll());
        tvJobs.setItems(jobs);
        tbJobId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getJobId()));
        tbClient.setCellValueFactory(cellData -> cellData.getValue().getJobDetails().getClient().getFullName());
        tbEstimatedLabourTime.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getJobDetails().getEstimatedLabourTime()));
        tbCollectedDate.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getJobDetails().getDateCollected()));
        tbReturnDate.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getJobDetails().getReturnDate()));
        tbJobApproved.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isJobApproved()));
    }

    @FXML
    private void loadJob(){
        JobModel job = tvJobs.getSelectionModel().getSelectedItem();
    }
}
