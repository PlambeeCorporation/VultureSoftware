package com.plambeeco.view.recordjobviews;

import com.plambeeco.dataaccess.dataprocessor.PartModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.helper.TextFieldHelper;
import com.plambeeco.models.IPartModel;
import com.plambeeco.models.PartModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RecordPartsNeededController {
    private final String ADD_NEW_PART = "Add new Part";

    @FXML
    private ComboBox<String> cbPartsNeeded;
    @FXML
    private TextField txtPartsQuantity;
    @FXML
    private TableView<IPartModel> tvPartsView;
    @FXML
    private TableColumn<IPartModel, String> partNameColumn;
    @FXML
    private TableColumn<IPartModel, Number> partQuantityColumn;

    private ObservableList<IPartModel> partsNeeded;

    private Stage primaryStage;

    public List<IPartModel> getPartsNeeded(){
        return partsNeeded;
    }

    @FXML
    private void initialize(){
        initializeTableViewAndColumns();
        initializeCBPartsNeeded();
        TextFieldHelper.allowNumbersOnly(txtPartsQuantity);
    }

    private void initializeCBPartsNeeded(){
        if(!cbPartsNeeded.getItems().isEmpty()){
            cbPartsNeeded.getItems().clear();
        }

        cbPartsNeeded.setItems(getSortedPartNames());
    }

    /**
     * Sorts the part names list for the combo box.
     * @return sorted list of part names.
     */
    private ObservableList<String> getSortedPartNames(){
        ObservableList<String> sortedPartNames = FXCollections.observableArrayList(PartModelProcessor.getAllPartNames());
        sortedPartNames.sort((String::compareTo));
        sortedPartNames.add(0, ADD_NEW_PART);

        return sortedPartNames;
    }

    private void initializeTableViewAndColumns(){
        partsNeeded = FXCollections.observableArrayList();
        tvPartsView.setItems(partsNeeded);
        partNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        partQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().partQuantityProperty());
    }

    public void setRootScene(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    @FXML
    private void createPart(){
        if(validatePartModelInput()){
            String partName = cbPartsNeeded.getValue();
            int partQuantity = Integer.valueOf(txtPartsQuantity.getText());
            partsNeeded.add(new PartModel(partName, partQuantity));
        }
    }

    private boolean validatePartModelInput(){
        boolean isValid = true;
        String errorMessage = "";

        if(cbPartsNeeded.getSelectionModel().isEmpty() || cbPartsNeeded.getValue().equals("Create new Part")){
            isValid = false;
            errorMessage += "You need to choose a part!\n";
        }

        if(txtPartsQuantity.getText() == null || txtPartsQuantity.getLength() <= 0){
            isValid = false;
            errorMessage += "You need to enter part quantity!\n";

        }else if(Integer.valueOf(txtPartsQuantity.getText()) <= 0){
            isValid = false;
            errorMessage += "The part quantity must be bigger than 0!\n";
        }

        if(!isValid){
            AlertHelper.showAlert(primaryStage, "Error Adding New Part!", errorMessage);
        }

        return isValid;
    }

    @FXML
    private void removePart(){
        int selectedIndex = tvPartsView.getSelectionModel().getSelectedIndex();

        if(selectedIndex >= 0){
            tvPartsView.getItems().remove(selectedIndex);
        }else{
            AlertHelper.showAlert(primaryStage, "Error Removing Part", "Select a part to remove!");
        }
    }

    @FXML
    private void createNewPartName(){
        if(cbPartsNeeded.getValue().equals(ADD_NEW_PART)){
            createPartEditDialogView(ADD_NEW_PART, null);
            initializeCBPartsNeeded();
            cbPartsNeeded.getSelectionModel().selectLast();
        }
    }

    @FXML
    private void editPartName(){
        String partName = cbPartsNeeded.getValue();
        int index = cbPartsNeeded.getSelectionModel().getSelectedIndex();
        if(!partName.equals(ADD_NEW_PART)){
            createPartEditDialogView("Edit Part", partName);
            initializeCBPartsNeeded();
            cbPartsNeeded.getSelectionModel().select(index);
        }
    }

    private void createPartEditDialogView(String title, String partName){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RecordPartsNeededController.class.getResource("parteditdialogview.fxml"));
            AnchorPane partEditDialogView = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            Scene scene = new Scene(partEditDialogView);
            dialogStage.setScene(scene);

            PartEditDialogViewController partEditDialogViewController = loader.getController();
            partEditDialogViewController.setDialogStage(dialogStage);
            partEditDialogViewController.setOldPartName(partName);
            ObservableSet<String> partNames = FXCollections.observableSet();
            partNames.addAll(cbPartsNeeded.getItems());
            partEditDialogViewController.setPartNames(partNames);

            dialogStage.showAndWait();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
