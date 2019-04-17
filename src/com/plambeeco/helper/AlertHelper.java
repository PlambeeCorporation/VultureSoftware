package com.plambeeco.helper;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertHelper {
    private AlertHelper() {
        //no instance
    }

    public static void showAlert(Stage dialogStage, String title, String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
