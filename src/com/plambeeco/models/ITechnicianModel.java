package com.plambeeco.models;

import javafx.beans.property.StringProperty;

import java.util.List;

public interface ITechnicianModel extends IPersonModel{
    List<ITaskModel> getCurrentTasks();

    void setCurrentTasks(List<ITaskModel> currentTasks);
    StringProperty getNamesOfCurrentlyAssignedTasks();
}
