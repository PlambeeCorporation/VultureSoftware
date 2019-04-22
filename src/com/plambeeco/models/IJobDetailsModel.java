package com.plambeeco.models;

import javafx.beans.property.IntegerProperty;

import java.time.LocalDate;
import java.util.List;

public interface IJobDetailsModel {
    int getJobDetailsId();

    void setJobDetailsId(int jobDetailsId);

    IPersonModel getClient();

    void setClient(IPersonModel client);

    ITechnicianModel getCheckedBy_Technician();

    void setCheckedBy_Technician(ITechnicianModel checkedBy_Technician);

    LocalDate getCheckingDate();

    void setCheckingDate(LocalDate checkingDate);

    LocalDate getDateCollected();

    void setDateCollected(LocalDate dateCollected);

    int getEstimatedLabourTime();

    IntegerProperty estimatedLabourTimeProperty();

    void setEstimatedLabourTime(int estimatedLabourTime);

    LocalDate getReturnDate();

    void setReturnDate(LocalDate returnDate);
}
