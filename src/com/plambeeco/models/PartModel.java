package com.plambeeco.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PartModel implements IPartModel {
    private int partId;
    private StringProperty partName;
    private IntegerProperty partQuantity;

    public PartModel(String partName, int partQuantity) {
        this.partName = new SimpleStringProperty(partName);
        this.partQuantity = new SimpleIntegerProperty(partQuantity);
    }


    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    @Override
    public String getPartName() {
        return partName.get();
    }

    @Override
    public StringProperty partNameProperty() {
        return partName;
    }

    @Override
    public void setPartName(String partName) {
        this.partName.set(partName);
    }

    @Override
    public int getPartQuantity() {
        return partQuantity.get();
    }

    @Override
    public IntegerProperty partQuantityProperty() {
        return partQuantity;
    }

    @Override
    public void setPartQuantity(int partQuantity) {
        this.partQuantity.set(partQuantity);
    }

    @Override
    public String toString() {
        return  "ID: " + getPartId() +
                ", Part Name: " + getPartName() +
                ", Quantity: " + getPartQuantity();
    }
}
