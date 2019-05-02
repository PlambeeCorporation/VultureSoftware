package com.plambeeco.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class PersonModel implements IPersonModel {
    protected int personId;
    protected StringProperty forename;
    protected StringProperty surname;
    protected StringProperty emailAddress;
    protected StringProperty phoneNumber;

    public PersonModel(String forename, String surname, String emailAddress, String phoneNumber) {
        this.forename = new SimpleStringProperty(forename);
        this.surname = new SimpleStringProperty(surname);
        this.emailAddress = new SimpleStringProperty(emailAddress);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);

    }

    @Override
    public int getPersonId() {
        return personId;
    }

    @Override
    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Override
    public String getForename() {
        return forename.get();
    }

    @Override
    public StringProperty forenameProperty() {
        return forename;
    }

    @Override
    public void setForename(String forename) {
        this.forename.set(forename);
    }

    @Override
    public String getSurname() {
        return surname.get();
    }

    @Override
    public StringProperty surnameProperty() {
        return surname;
    }

    @Override
    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    @Override
    public String getEmailAddress() {
        return emailAddress.get();
    }

    @Override
    public StringProperty emailAddressProperty() {
        return emailAddress;
    }

    @Override
    public void setEmailAddress(String emailAddress) {
        this.emailAddress.set(emailAddress);
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    @Override
    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    @Override
    public StringProperty getFullName(){
         String fullname = forename.get() + " " + surname.get();
         return new SimpleStringProperty(fullname);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonModel that = (PersonModel) o;
        return personId == that.personId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId);
    }

    @Override
    public String toString() {
        return
                "Forename: " + forename.get() +
                "\nSurname: " + surname.get() +
                "\nEmailAddress: " + emailAddress.get() +
                "\nPhoneNumber: " + phoneNumber.get() +
                '\n';
    }
}
