package com.plambeeco.view.jobviews.partsview;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PartDetailsViewController {
    @FXML
    private ComboBox<String> cbPartNames;
    @FXML
    private TextField txtQuantity;
    
    private Stage partDetailsStage;

    public void setPartDetailsStage(Stage partDetailsStage) {
        this.partDetailsStage = partDetailsStage;
    }



    @FXML
    private void handleOK(){
        if(isInputValid()){

        }

        partDetailsStage.close();
    }

    @FXML
    private void handleCancel(){
        partDetailsStage.close();
    }

    private boolean isInputValid() {
        return true;
    }
}
