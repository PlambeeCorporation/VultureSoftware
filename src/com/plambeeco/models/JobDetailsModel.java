package com.plambeeco.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.time.LocalDate;

public class JobDetailsModel implements IJobDetailsModel {
    private int jobDetailsId;
    private IPersonModel client;
    private ITechnicianModel checkedBy_Technician;
    private LocalDate checkingDate;
    private LocalDate dateCollected;
    private IntegerProperty estimatedLabourTime;
    private LocalDate returnDate;

    public JobDetailsModel(IPersonModel client, ITechnicianModel checkedBy_Technician, LocalDate checkingDate,
                           LocalDate dateCollected, LocalDate returnDate) {
        this.client = client;
        this.checkedBy_Technician = checkedBy_Technician;
        this.checkingDate = checkingDate;
        this.dateCollected = dateCollected;
        this.returnDate = returnDate;
        this.estimatedLabourTime = new SimpleIntegerProperty();
    }

    public JobDetailsModel(IPersonModel client, ITechnicianModel checkedBy_Technician, LocalDate checkingDate,
                           LocalDate dateCollected, int estimatedLabourTime, LocalDate returnDate) {
        this.client = client;
        this.checkedBy_Technician = checkedBy_Technician;
        this.checkingDate = checkingDate;
        this.dateCollected = dateCollected;
        this.estimatedLabourTime = new SimpleIntegerProperty(estimatedLabourTime);
        this.returnDate = returnDate;
    }

    @Override
    public int getJobDetailsId() {
        return jobDetailsId;
    }

    @Override
    public void setJobDetailsId(int jobDetailsId) {
        this.jobDetailsId = jobDetailsId;
    }

    @Override
    public IPersonModel getClient() {
        return client;
    }

    @Override
    public void setClient(IPersonModel client) {
        this.client = client;
    }

    @Override
    public ITechnicianModel getCheckedBy_Technician() {
        return checkedBy_Technician;
    }

    @Override
    public void setCheckedBy_Technician(ITechnicianModel checkedBy_Technician) {
        this.checkedBy_Technician = checkedBy_Technician;
    }

    @Override
    public LocalDate getCheckingDate() {
        return checkingDate;
    }

    @Override
    public void setCheckingDate(LocalDate checkingDate) {
        this.checkingDate = checkingDate;
    }

    @Override
    public LocalDate getDateCollected() {
        return dateCollected;
    }

    @Override
    public void setDateCollected(LocalDate dateCollected) {
        this.dateCollected = dateCollected;
    }

    @Override
    public int getEstimatedLabourTime() {
        return estimatedLabourTime.get();
    }

    @Override
    public IntegerProperty estimatedLabourTimeProperty() {
        return estimatedLabourTime;
    }

    @Override
    public void setEstimatedLabourTime(int estimatedLabourTime) {
        this.estimatedLabourTime.set(estimatedLabourTime);
    }

    @Override
    public LocalDate getReturnDate() {
        return returnDate;
    }

    @Override
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "JobDetailsModel{" +
                "client=" + client +
                ", checkedBy_Technician=" + checkedBy_Technician +
                ", checkingDate=" + checkingDate +
                ", dateCollected=" + dateCollected +
                ", estimatedLabourTime=" + estimatedLabourTime +
                ", returnDate=" + returnDate +
                '}';
    }
}
