package com.plambeeco.dataaccess.dataprocessor;

import com.plambeeco.dataaccess.repository.IPersonModelRepository;
import com.plambeeco.dataaccess.repository.PersonModelRepository;
import com.plambeeco.models.IPersonModel;
import com.plambeeco.models.ITechnicianModel;
import com.plambeeco.models.TechnicianModel;

import java.util.List;

public class PersonModelProcessor {
    //TODO - Add proper validation.

    private PersonModelProcessor() {
        throw new RuntimeException("This class should not be initialized!");
    }

    public static void add(IPersonModel personModel) {
        IPersonModelRepository personRepository = new PersonModelRepository();
        personRepository.add(personModel);
    }


    public static void update(IPersonModel personModel) {
        IPersonModelRepository personRepository = new PersonModelRepository();
        personRepository.update(personModel);
    }

    public static void remove(IPersonModel personModel) {
        IPersonModelRepository personRepository = new PersonModelRepository();
        personRepository.remove(personModel);
    }

    public static IPersonModel getById(int id) {
        IPersonModelRepository personRepository = new PersonModelRepository();
        return personRepository.getById(id);
    }

    public static ITechnicianModel getTechnicianById(int id){
        IPersonModel technician = getById(id);
        return new TechnicianModel(technician, TaskModelProcessor.getTechniciansCurrentlyAssignedTasks(id));
    }

    public static List<IPersonModel> getAll() {
        IPersonModelRepository personRepository = new PersonModelRepository();
        return personRepository.getAll();
    }

    public static List<ITechnicianModel> getAllTechnicians(){
        IPersonModelRepository personRepository = new PersonModelRepository();
        return personRepository.getAllTechnicians();
    }

    public static List<IPersonModel> getAllClients(){
        IPersonModelRepository personRepository = new PersonModelRepository();
        return personRepository.getAllClients();
    }

    public static List<ITechnicianModel> getAssignedTechnicians(int taskId){
        IPersonModelRepository personRepository = new PersonModelRepository();
        return personRepository.getAssignedTechnicians(taskId);
    }
}
