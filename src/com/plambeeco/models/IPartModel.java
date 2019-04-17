package com.plambeeco.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public interface IPartModel {
    int getPartId();

    void setPartId(int partId);
    String getPartName();

    StringProperty partNameProperty();

    void setPartName(String partName);

    int getPartQuantity();

    IntegerProperty partQuantityProperty();

    void setPartQuantity(int partQuantity);
}
