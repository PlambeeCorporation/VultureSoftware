package com.plambeeco.view.jobviews.partsview;

import com.plambeeco.dataaccess.dataprocessor.PartModelProcessor;
import com.plambeeco.models.IPartModel;
import com.plambeeco.models.JobModel;
import com.plambeeco.models.PartModel;
import com.plambeeco.view.recordjobviews.PartEditDialogViewController;
import com.plambeeco.view.recordjobviews.RecordPartsNeededController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PartDetailsViewController {
    private final String ADD_NEW_PART = "Add new Part Name";
    @FXML
    private ComboBox<String> cbPartNames;
    @FXML
    private TextField txtQuantity;
    
    private Stage partDetailsStage;
    private IPartModel partModel;
    private JobModel currentJob;

    public PartDetailsViewController(Stage partDetailsStage, IPartModel partModel, JobModel currentJob) {
        this.partDetailsStage = partDetailsStage;
        this.partModel = partModel;
        this.currentJob = currentJob;
    }

    @FXML
    private void initialize(){
        initializeComboBox();
    }

    private void initializeComboBox(){
        ObservableList<String> sortedPartNames = FXCollections.observableArrayList(PartModelProcessor.getAllPartNames());
        sortedPartNames.sort((String::compareTo));
        sortedPartNames.add(0, ADD_NEW_PART);
        cbPartNames.setItems(sortedPartNames);

        if(partModel != null){
            cbPartNames.setValue(partModel.getPartName());
            txtQuantity.setText(String.valueOf(partModel.getPartQuantity()));
        }
    }

    @FXML
    private void loadAddNewPartNameView(){
        if(cbPartNames.getValue().equals(ADD_NEW_PART)){
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(RecordPartsNeededController.class.getResource("parteditdialogview.fxml"));
                AnchorPane partEditDialogView = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Add new Part");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(partDetailsStage);

                Scene scene = new Scene(partEditDialogView);
                dialogStage.setScene(scene);

                PartEditDialogViewController partEditDialogViewController = loader.getController();
                partEditDialogViewController.setDialogStage(dialogStage);

                //Converts part names list into a set, and remove the add new part functionality.
                ObservableSet<String> partNames = FXCollections.observableSet();
                partNames.addAll(cbPartNames.getItems());
                partNames.remove(ADD_NEW_PART);
                partEditDialogViewController.setPartNames(partNames);

                dialogStage.showAndWait();
                initializeComboBox();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleOK(){
        if(isInputValid()){
            String partName = cbPartNames.getValue();
            int quantity = Integer.valueOf(txtQuantity.getText());
            if(partModel != null){
                partModel.setPartName(partName);
                partModel.setPartQuantity(quantity);
            }else{
                IPartModel newPartModel = new PartModel(partName, quantity);
                currentJob.getPartsNeeded().add(newPartModel);
            }
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
