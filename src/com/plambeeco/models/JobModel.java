package com.plambeeco.models;

import java.time.LocalDate;
import java.util.List;

public class JobModel {
    private int jobId;

    private IMotorModel motor;
    private IJobDetailsModel jobDetails;
    private List<IPartModel> partsNeeded;
    private List<ITaskModel> jobTasks;
    private ITechnicianModel inspectingTechnician;
    private LocalDate inspectionDate;
    private boolean jobApproved;

    private JobModel(JobBuilder jobBuilder){
        jobId = jobBuilder.jobId;
        motor = jobBuilder.motor;
        jobDetails = jobBuilder.jobDetails;
        partsNeeded = jobBuilder.partsNeeded;
        jobTasks = jobBuilder.jobTasks;
        inspectingTechnician = jobBuilder.inspectingTechnician;
        inspectionDate = jobBuilder.inspectionDate;
        jobApproved = jobBuilder.jobApproved;
    }

    public void setJobId(int jobId){
        this.jobId = jobId;
    }

    public int getJobId() {
        return jobId;
    }

    public IMotorModel getMotor() {
        return motor;
    }

    public IJobDetailsModel getJobDetails() {
        return jobDetails;
    }

    public List<IPartModel> getPartsNeeded() {
        return partsNeeded;
    }

    public List<ITaskModel> getJobTasks() {
        return jobTasks;
    }

    public ITechnicianModel getInspectingTechnician() {
        return inspectingTechnician;
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public boolean isJobApproved() {
        return jobApproved;
    }

    @Override
    public String toString() {
        return "JobModel{" +
                "\njobId=" + jobId +
                "\nmotorModel= " + motor +
                "\nJobDetailsModel= " + jobDetails +
                "\npartsNeeded=" + partsNeeded +
                "\njobTasks=" + jobTasks +
                "\ninspectingTechnician=" + inspectingTechnician +
                "\ninspectionDate=" + inspectionDate +
                "\njobApproved=" + jobApproved +
                '}';
    }

    public static class JobBuilder{
        private int jobId;
        private IMotorModel motor;
        private IJobDetailsModel jobDetails;
        private List<IPartModel> partsNeeded;
        private List<ITaskModel> jobTasks;
        private ITechnicianModel inspectingTechnician;
        private LocalDate inspectionDate;
        private boolean jobApproved;

        public JobBuilder(){}

        public JobBuilder setJobId(int jobId){
            this.jobId = jobId;
            return this;
        }

        public JobBuilder setMotor(IMotorModel motor){
            this.motor = motor;
            return this;
        }

        public JobBuilder setJobDetails(IJobDetailsModel jobDetails){
            this.jobDetails = jobDetails;
            return this;
        }

        public JobBuilder setPartsNeeded(List<IPartModel> partsNeeded){
            this.partsNeeded = partsNeeded;
            return this;
        }

        public JobBuilder setJobTasks(List<ITaskModel> jobTasks){
            this.jobTasks = jobTasks;
            return this;
        }

        public JobBuilder setInspectingTechnician(ITechnicianModel inspectingTechnician){
            this.inspectingTechnician = inspectingTechnician;
            return this;
        }

        public JobBuilder setInspectionDate(LocalDate inspectionDate){
            this.inspectionDate = inspectionDate;
            return this;
        }

        public JobBuilder setJobApproved(boolean jobApproved){
            this.jobApproved = jobApproved;
            return this;
        }

        public JobModel build(){
            return new JobModel(this);
        }
    }
}
