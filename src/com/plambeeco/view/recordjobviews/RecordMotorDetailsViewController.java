package com.plambeeco.view.recordjobviews;

import com.plambeeco.helper.AlertHelper;
import com.plambeeco.models.IMotorModel;
import com.plambeeco.models.MotorModel;
import com.plambeeco.view.RootTechnicianController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RecordMotorDetailsViewController {
    @FXML
    private TextField txtMotorType;
    @FXML
    private TextField txtManufacturer;
    @FXML
    private TextField txtEstimatedYear;

    /**
     * Creates new motor model.
     * @return motor model.
     */
    @FXML
    public IMotorModel createMotorModel(){
        if(validateMotorModelInput()){
            String motorType = txtMotorType.getText();
            String manufacturer = txtManufacturer.getText();
            int estimatedYear = Integer.valueOf(txtEstimatedYear.getText());

            return new MotorModel(motorType, manufacturer, estimatedYear);
        }

        return null;
    }

    /**
     * Validates motor details.
     * @return true if details are valid, false otherwise.
     */
    private boolean validateMotorModelInput(){
        boolean isValid = true;

        String errorMessage = "";

        if(txtMotorType.getText() == null || txtMotorType.getLength() <= 0){
            isValid = false;
            errorMessage += "You need to enter motor name\n";
        }

        if(txtManufacturer.getText() == null || txtManufacturer.getLength() <= 0){
            isValid = false;
            errorMessage += "You need to enter motor manufacturer\n";
        }

        if(txtEstimatedYear.getText() == null || txtEstimatedYear.getLength() != 4){
            isValid = false;
            errorMessage += "You need to enter motor estimated year production\n";
        }

        if(!isValid){
            AlertHelper.showAlert(RootTechnicianController.getPrimaryStage(), "Invalid Motor Details", errorMessage);
        }

        return isValid;
    }
}
