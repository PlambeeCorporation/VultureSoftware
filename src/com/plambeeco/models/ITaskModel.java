package com.plambeeco.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public interface ITaskModel {
    int getTaskId();

    void setTaskId(int taskId);

    int getJobId();

    IntegerProperty jobIdProperty();

    void setJobId(int jobId);

    String getTaskName();

    StringProperty taskNameProperty();

    void setTaskName(String taskName);

    String getTaskPriority();

    StringProperty taskPriorityProperty();

    void setTaskPriority(String taskPriority);

    String getTaskNotes();

    StringProperty taskNotesProperty();

    void setTaskNotes(String taskNotes);

    int getHoursNeeded();

    IntegerProperty hoursNeededProperty();

    void setHoursNeeded(int hoursNeeded);

    List<ITechnicianModel> getAssignedTechnicians();

    void setAssignedTechnicians(List<ITechnicianModel> assignedTechnicians);

    StringProperty getFullNamesOfAssignedTechnicians();

    void addTechnician(ITechnicianModel technicianModel);

    boolean isTaskCompleted();

    void setTaskCompleted(boolean taskCompleted);
}
