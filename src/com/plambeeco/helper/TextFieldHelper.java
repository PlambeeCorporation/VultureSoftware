package com.plambeeco.helper;

import javafx.scene.control.TextField;

public class TextFieldHelper {
    private TextFieldHelper() {
        throw new RuntimeException("This class cannot be initialized");
    }

    public static void allowNumbersOnly(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")){
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
