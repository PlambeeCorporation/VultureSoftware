package com.plambeeco.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.time.LocalDate;
import java.util.List;

public class JobDetailsModel implements IJobDetailsModel {
    private int jobDetailsId;
    private IPersonModel client;
    private ITechnicianModel checkedBy_Technician;
    private LocalDate checkingDate;
    private LocalDate dateCollected;
    private IntegerProperty estimatedLabourTime;
    private LocalDate returnDate;

    /**
     * Creates new JobDetailModel object.
     * @param client reference to the client object.
     * @param checkedBy_Technician referecence to the checking technician object.
     * @param checkingDate checking date.
     * @param dateCollected date collected.
     * @param returnDate return date.
     */
    public JobDetailsModel(IPersonModel client, ITechnicianModel checkedBy_Technician, LocalDate checkingDate,
                           LocalDate dateCollected, LocalDate returnDate) {
        this.client = client;
        this.checkedBy_Technician = checkedBy_Technician;
        this.checkingDate = checkingDate;
        this.dateCollected = dateCollected;
        this.returnDate = returnDate;
        this.estimatedLabourTime = new SimpleIntegerProperty();
    }

    /**
     * Creates new JobDetailModel object.
     * @param client reference to the client object.
     * @param checkedBy_Technician referecence to the checking technician object.
     * @param checkingDate checking date.
     * @param dateCollected date collected.
     * @param estimatedLabourTime estimated labour time.
     * @param returnDate return date.
     */
    public JobDetailsModel(IPersonModel client, ITechnicianModel checkedBy_Technician, LocalDate checkingDate,
                           LocalDate dateCollected, int estimatedLabourTime, LocalDate returnDate) {
        this.client = client;
        this.checkedBy_Technician = checkedBy_Technician;
        this.checkingDate = checkingDate;
        this.dateCollected = dateCollected;
        this.estimatedLabourTime = new SimpleIntegerProperty(estimatedLabourTime);
        this.returnDate = returnDate;
    }

    /**
     * Get job details unique identification.
     * @return unique identification.
     */
    @Override
    public int getJobDetailsId() {
        return jobDetailsId;
    }

    /**
     * Set job details unique identification.
     * @param jobDetailsId unique identification.
     */
    @Override
    public void setJobDetailsId(int jobDetailsId) {
        this.jobDetailsId = jobDetailsId;
    }

    /**
     * Get client reference object.
     * @return client reference object.
     */
    @Override
    public IPersonModel getClient() {
        return client;
    }

    /**
     * Set client reference object.
     * @param client new client object.
     */
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


    public static int calculateEstimatedLabourTime(List<ITaskModel> tasksNeeded){
        int estimatedLabourTime = 0;

        for(ITaskModel taskModel : tasksNeeded){
            estimatedLabourTime += taskModel.getHoursNeeded();
        }

        return estimatedLabourTime;
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
