package com.plambeeco.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public interface IMotorModel {
    int getMotorId();

    void setMotorId(int motorId);

    String getMotorType();

    StringProperty motorTypeProperty();

    void setMotorType(String motorType);

    String getManufacturer();

    StringProperty manufacturerProperty();

    void setManufacturer(String manufacturer);

    int getEstimatedYearOfManufacture();

    IntegerProperty estimatedYearOfManufactureProperty();

    void setEstimatedYearOfManufacture(int estimatedYearOfManufacture);
}
