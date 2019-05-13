package com.plambeeco.view.recordjobviews;


import com.plambeeco.VultureApplication;
import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.helper.AlertHelper;
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


    void setrootJobRecordController(){
    }

    @FXML
    private void initialize(){
        cbClient.getItems().addAll(PersonModelProcessor.getAllClients());
        cbTechnicians.getItems().addAll(PersonModelProcessor.getAllTechnicians());
    }

    IJobDetailsModel createJobDetails(){
        if(validateJobDetails()){
            IPersonModel client = cbClient.getValue();
            ITechnicianModel checkingTechnician = cbTechnicians.getValue();
            LocalDate checkingDate = dpCheckingDate.getValue();
            LocalDate dateCollected = dpDateCollected.getValue();
            LocalDate returnDate = dpReturnDate.getValue();

            return new JobDetailsModel(client, checkingTechnician, checkingDate, dateCollected, returnDate);
        }
        return null;
    }

    private boolean validateJobDetails(){
        boolean isValid = true;
        String errorMessage = "";

        if(cbClient.getValue() == null){
            isValid = false;
            errorMessage += "Select a client";
        }

        if(cbTechnicians.getValue() == null){
            isValid = false;
            errorMessage += "Select a checking technician";
        }

        if(dpCheckingDate.getValue() == null){
            isValid = false;
            errorMessage += "Select a checking date";
        }

        if(dpDateCollected.getValue() == null){
            isValid = false;
            errorMessage += "Select a collection date";
        }

        if(dpReturnDate.getValue() == null){
            isValid = false;
            errorMessage += "Select a return date";
        }

        if(!isValid){
            AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Invalid Job Details", errorMessage);
        }

        return isValid;
    }
}
