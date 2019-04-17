package com.plambeeco.view.recordjobviews;

import com.plambeeco.models.IMotorModel;
import com.plambeeco.models.MotorModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RecordMotorDetailsViewController {
    @FXML
    private TextField txtMotorType;
    @FXML
    private TextField txtManufacturer;
    @FXML
    private TextField txtEstimatedYear;

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

        System.out.println(errorMessage);

        return isValid;
    }
}
