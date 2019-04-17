package com.plambeeco.view.recordjobviews;


import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.models.IJobDetailsModel;
import com.plambeeco.models.IPersonModel;
import com.plambeeco.models.ITechnicianModel;
import com.plambeeco.models.JobDetailsModel;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;


public class RecordJobDetailsViewController {
    @FXML
    private ComboBox<IPersonModel> cbClient;
    @FXML
    private DatePicker dpDateCollected;
    @FXML
    private TextField txtEstimatedLabourTime;
    @FXML
    private DatePicker dpReturnDate;
    @FXML
    private ComboBox<ITechnicianModel> cbTechnicians;
    @FXML
    private DatePicker dpCheckingDate;

    private RootJobRecordController rootJobRecordController;

    public void setrootJobRecordController(RootJobRecordController rootJobRecordController){
        this.rootJobRecordController = rootJobRecordController;
    }

    @FXML
    private void initialize(){
        cbClient.getItems().addAll(PersonModelProcessor.getAllClients());
        cbTechnicians.getItems().addAll(PersonModelProcessor.getAllTechnicians());
    }

    public IJobDetailsModel getJobDetails(){
        IPersonModel client = cbClient.getValue();
        ITechnicianModel checkingTechnician = cbTechnicians.getValue();
        LocalDate checkingDate = dpCheckingDate.getValue();
        LocalDate dateCollected = dpDateCollected.getValue();
        LocalDate returnDate = dpReturnDate.getValue();

        return new JobDetailsModel(client, checkingTechnician, checkingDate, dateCollected, returnDate);
    }

}
