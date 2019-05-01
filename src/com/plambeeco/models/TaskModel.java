package com.plambeeco.models;

import com.plambeeco.dataaccess.dataprocessor.JobModelProcessor;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskModel implements ITaskModel {
    private int taskId;
    private IntegerProperty jobId;
    private StringProperty taskName;
    private StringProperty taskPriority;
    private StringProperty taskNotes;
    private IntegerProperty hoursNeeded;
    private List<ITechnicianModel> assignedTechnicians;
    private boolean taskCompleted;

    public TaskModel(String taskName, String taskPriority, String taskNotes, int hoursNeeded){
        this.taskName = new SimpleStringProperty(taskName);
        this.taskPriority = new SimpleStringProperty(taskPriority);
        this.hoursNeeded = new SimpleIntegerProperty(hoursNeeded);
        this.taskNotes = new SimpleStringProperty(taskNotes);
        this.assignedTechnicians = new ArrayList<>();
        this.taskCompleted = false;
        this.jobId = new SimpleIntegerProperty();
    }

    public TaskModel(String taskName, String taskPriority, String taskNotes, int hoursNeeded, boolean taskCompleted){
        this.taskName = new SimpleStringProperty(taskName);
        this.taskPriority = new SimpleStringProperty(taskPriority);
        this.hoursNeeded = new SimpleIntegerProperty(hoursNeeded);
        this.taskNotes = new SimpleStringProperty(taskNotes);
        this.assignedTechnicians = new ArrayList<>();
        this.taskCompleted = taskCompleted;
        this.jobId = new SimpleIntegerProperty();
    }

    public TaskModel(String taskName, String taskPriority, String taskNotes, int hoursNeeded,
                     List<ITechnicianModel> assignedTechnicians, boolean taskCompleted) {
        this.taskName = new SimpleStringProperty(taskName);
        this.taskPriority = new SimpleStringProperty(taskPriority);
        this.taskNotes = new SimpleStringProperty(taskNotes);
        this.hoursNeeded = new SimpleIntegerProperty(hoursNeeded);
        this.assignedTechnicians = assignedTechnicians;
        this.taskCompleted = taskCompleted;
        this.jobId = new SimpleIntegerProperty();
    }

    @Override
    public int getTaskId() {
        return taskId;
    }

    @Override
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public int getJobId() {
        return jobId.get();
    }

    @Override
    public IntegerProperty jobIdProperty() {
        return jobId;
    }

    @Override
    public void setJobId(int jobId) {
        this.jobId.set(jobId);
    }

    @Override
    public String getTaskName() {
        return taskName.get();
    }

    @Override
    public StringProperty taskNameProperty() {
        return taskName;
    }

    @Override
    public void setTaskName(String taskName) {
        this.taskName.set(taskName);
    }

    @Override
    public String getTaskPriority() {
        return taskPriority.get();
    }

    @Override
    public StringProperty taskPriorityProperty() {
        return taskPriority;
    }

    @Override
    public void setTaskPriority(String taskPriority) {
        this.taskPriority.set(taskPriority);
    }

    @Override
    public String getTaskNotes() {
        return taskNotes.get();
    }

    @Override
    public StringProperty taskNotesProperty() {
        return taskNotes;
    }

    @Override
    public void setTaskNotes(String taskNotes) {
        this.taskNotes.set(taskNotes);
    }

    @Override
    public int getHoursNeeded() {
        return hoursNeeded.get();
    }

    @Override
    public IntegerProperty hoursNeededProperty() {
        return hoursNeeded;
    }

    @Override
    public void setHoursNeeded(int hoursNeeded) {
        this.hoursNeeded.set(hoursNeeded);
    }

    @Override
    public List<ITechnicianModel> getAssignedTechnicians() {
        return assignedTechnicians;
    }

    @Override
    public void setAssignedTechnicians(List<ITechnicianModel> assignedTechnicians) {
        this.assignedTechnicians = assignedTechnicians;
    }

    @Override
    public StringProperty getFullNamesOfAssignedTechnicians(){
       if(assignedTechnicians == null || assignedTechnicians.size() < 1){
           return new SimpleStringProperty("");
       }

        StringBuilder output = new StringBuilder();

        for(ITechnicianModel technician : assignedTechnicians){
            output.append(technician.getFullName().get())
                  .append("\n");
        }

        return new SimpleStringProperty(output.toString());
    }

    @Override
    public void addTechnician(ITechnicianModel technicianModel){
        assignedTechnicians.add(technicianModel);
    }

    @Override
    public boolean isTaskCompleted() {
        return taskCompleted;
    }

    @Override
    public void setTaskCompleted(boolean taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    @Override
    public LocalDate getTaskFinishDate(){
        JobModel job = JobModelProcessor.getById(jobId.get());
        return job.getJobDetails().getReturnDate();
    }

    @Override
    public boolean isTaskOverdue(){
        if(!taskCompleted){
            LocalDate currentDate = LocalDate.now();
            return getTaskFinishDate().isAfter(currentDate);
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskModel taskModel = (TaskModel) o;
        return taskId == taskModel.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "taskId=" + taskId +
                ", taskName=" + taskName +
                ", taskPriority=" + taskPriority +
                ", taskNotes=" + taskNotes +
                ", hoursNeeded=" + hoursNeeded +
                ", assignedTechnicians=" + assignedTechnicians +
                ", taskCompleted=" + taskCompleted +
                '}';
    }
}
