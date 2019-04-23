package com.plambeeco.view.recordjobviews;

import com.plambeeco.dataaccess.dataprocessor.PartModelProcessor;
import com.plambeeco.helper.AlertHelper;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This class handles adding
 */

public class PartEditDialogViewController {
    @FXML
    private TextField txtPartName;
    private ObservableSet<String> partNames;

    private Stage dialogStage;
    private String oldPartName;

    public void setOldPartName(String oldPartName){
        this.oldPartName = oldPartName;
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public void setPartNames(ObservableSet<String> partNames){
        this.partNames = partNames;
    }


    /**
     * When OK button is pressed, checks if the input data is valid, then asks
     * ListOfPartNames class to create new part with the name written in the
     * txtPartName TextField.
     */
    @FXML
    private void handleOK(){
        if(isInputValid()){
            String partName = txtPartName.getText();

            if(oldPartName == null){
                if(!partNames.add(partName)){
                    AlertHelper.showAlert(dialogStage, "Duplicate part name", "A part with this name already exists.");
                }else{
                    PartModelProcessor.addPartName(partName);
                }
            }else{
                PartModelProcessor.updatePartName(oldPartName, partName);
                partNames.remove(oldPartName);
                partNames.add(partName);
            }


            dialogStage.close();
        }
    }

    /**
     * Closes the dialogStage.
     */
    @FXML
    private void handleCancel(){
        dialogStage.close();
    }

    /**
     * Validates the input. If the txtPartName TextField is empty,
     * show Alert.
     * @return  true if input is valid, false otherwise.
     */
    private boolean isInputValid(){
        if(txtPartName.getText() == null || txtPartName.getLength() <= 0){
            AlertHelper.showAlert(dialogStage, "Invalid part name", "Enter valid part name");
            return false;
        }

        return true;
    }
}
