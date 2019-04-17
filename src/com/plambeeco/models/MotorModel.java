package com.plambeeco.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MotorModel implements IMotorModel {
    private int motorId;
    private StringProperty motorType;
    private StringProperty manufacturer;
    private IntegerProperty estimatedYearOfManufacture;

    public MotorModel(String motorType, String manufacturer, int estimatedYearOfManufacture) {
        this.motorType = new SimpleStringProperty(motorType);
        this.manufacturer = new SimpleStringProperty(manufacturer);
        this.estimatedYearOfManufacture = new SimpleIntegerProperty(estimatedYearOfManufacture);
    }

    @Override
    public int getMotorId() {
        return motorId;
    }

    @Override
    public void setMotorId(int motorId) {
        this.motorId = motorId;
    }

    @Override
    public String getMotorType() {
        return motorType.get();
    }

    @Override
    public String toString() {
        return  "motorId:=" + motorId +
                ", motorType=" + motorType +
                ", manufacturer=" + manufacturer +
                ", estimatedYearOfManufacture=" + estimatedYearOfManufacture;

    }

    @Override
    public StringProperty motorTypeProperty() {
        return motorType;
    }

    @Override
    public void setMotorType(String motorType) {
        this.motorType.set(motorType);
    }

    @Override
    public String getManufacturer() {
        return manufacturer.get();
    }

    @Override
    public StringProperty manufacturerProperty() {
        return manufacturer;
    }

    @Override
    public void setManufacturer(String manufacturer) {
        this.manufacturer.set(manufacturer);
    }

    @Override
    public int getEstimatedYearOfManufacture() {
        return estimatedYearOfManufacture.get();
    }

    @Override
    public IntegerProperty estimatedYearOfManufactureProperty() {
        return estimatedYearOfManufacture;
    }

    @Override
    public void setEstimatedYearOfManufacture(int estimatedYearOfManufacture) {
        this.estimatedYearOfManufacture.set(estimatedYearOfManufacture);
    }
}
