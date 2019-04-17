package com.plambeeco.models;

import javafx.beans.property.StringProperty;

public interface IPersonModel {
    int getPersonId();

    void setPersonId(int personId);

    String getForename();

    StringProperty forenameProperty();

    void setForename(String forename);

    String getSurname();

    StringProperty surnameProperty();

    void setSurname(String surname);

    String getEmailAddress();

    StringProperty emailAddressProperty();

    void setEmailAddress(String emailAddress);

    String getPhoneNumber();

    StringProperty phoneNumberProperty();

    void setPhoneNumber(String phoneNumber);

    StringProperty getFullName();
}
