package com.plambeeco.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class TechnicianModel extends PersonModel implements ITechnicianModel {
    private List<ITaskModel> currentTasks; //List of current tasks technician is assigned to.

    /**
     * Constructor for technician
     * @param forename  First name of technician.
     * @param surname   Last name of technician.
     * @param emailAddress  Technician's email address.
     * @param phoneNumber   Technician's phone number.
     */
    public TechnicianModel(String forename, String surname, String emailAddress, String phoneNumber) {
        super(forename, surname, emailAddress, phoneNumber);
    }

    public TechnicianModel(String forename, String surname, String emailAddress, String phoneNumber,
                           List<ITaskModel> currentTasks) {
        super(forename, surname, emailAddress, phoneNumber);
        this.currentTasks = currentTasks;
    }

    public TechnicianModel(int id, IPersonModel technician, List<ITaskModel> currentTasks) {
        super(technician.getForename(), technician.getSurname(), technician.getEmailAddress(), technician.getPhoneNumber());
        this.personId = id;
        this.currentTasks = currentTasks;
    }

    /**
     *
     * @return //List of current tasks technician is assigned to.
     */
    @Override
    public List<ITaskModel> getCurrentTasks() {
        return currentTasks;
    }

    @Override
    public void setCurrentTasks(List<ITaskModel> currentTasks) {
        this.currentTasks = currentTasks;
    }

    @Override
    public StringProperty getNamesOfCurrentlyAssignedTasks(){
        if(currentTasks == null || currentTasks.size() < 1){
            return new SimpleStringProperty("");
        }

        StringBuilder output = new StringBuilder();

        for(ITaskModel task : currentTasks){
            output.append(task.getTaskName())
                    .append("\n");
        }

        return new SimpleStringProperty(output.toString());
    }
}
