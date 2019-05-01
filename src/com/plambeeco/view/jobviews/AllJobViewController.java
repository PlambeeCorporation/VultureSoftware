package com.plambeeco.view.jobviews;

import com.plambeeco.dataaccess.dataprocessor.JobModelProcessor;
import com.plambeeco.helper.ViewHelper;
import com.plambeeco.models.JobModel;
import com.plambeeco.view.RootTechnicianController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;

public class AllJobViewController {
    @FXML
    TableView<JobModel> tvJobs;
    @FXML
    TableColumn<JobModel, Number> tcJobId;
    @FXML
    TableColumn<JobModel, String> tcClient;
    @FXML
    TableColumn<JobModel, Number> tcEstimatedLabourTime;
    @FXML
    TableColumn<JobModel, LocalDate> tcCollectedDate;
    @FXML
    TableColumn<JobModel, LocalDate> tcReturnDate;
    @FXML
    TableColumn<JobModel, Boolean> tcJobApproved;

    private BorderPane rootScene;

    public void setRootScene(BorderPane rootScene) {
        this.rootScene = rootScene;
    }

    @FXML
    private void initialize(){
        ObservableList<JobModel> jobs = FXCollections.observableArrayList(JobModelProcessor.getAll());
        tvJobs.setItems(jobs);
        tcJobId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getJobId()));
        tcClient.setCellValueFactory(cellData -> cellData.getValue().getJobDetails().getClient().getFullName());
        tcEstimatedLabourTime.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getJobDetails().getEstimatedLabourTime()));
        tcCollectedDate.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getJobDetails().getDateCollected()));
        tcReturnDate.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getJobDetails().getReturnDate()));
        tcJobApproved.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isJobApproved()));
        tcJobApproved.setCellFactory(cellData -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                        item ? "Approved" : "Not Approved");
            }
        });
    }

    @FXML
    private void loadJob(){
        JobModel job = tvJobs.getSelectionModel().getSelectedItem();

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootTechnicianController.class.getResource(ViewHelper.JOB_VIEW_RESOURCE));
            JobViewController controller = new JobViewController(rootScene, job);
            loader.setController(controller);
            ViewHelper.getViewsResourcesStack().push(ViewHelper.ALL_JOBS_VIEW_RESOURCE);
            AnchorPane stage = loader.load();
            rootScene.setCenter(stage);


        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
