package com.plambeeco.dataaccess.dataprocessor;

import com.plambeeco.dataaccess.repository.ITaskModelRepository;
import com.plambeeco.dataaccess.repository.TaskModelRepository;
import com.plambeeco.models.ITaskModel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TaskModelProcessor {
    private TaskModelProcessor() {
        throw new RuntimeException("This class should not be initialized!");
    }

    public static void add(final ITaskModel taskModel) {
        if(validateTaskModel(taskModel)){
            ITaskModelRepository taskModelRepository = new TaskModelRepository();
            taskModelRepository.add(taskModel);
        }
    }

    public static void addTaskAssignedTechnician(ITaskModel task, int technicianId){
        if(validateTaskModel(task) && technicianId > 0){
            ITaskModelRepository taskModelRepository = new TaskModelRepository();
            taskModelRepository.addTaskAssignedTechnician(task, technicianId);
        }
    }

    public static void addTasksNeeded(int jobId, Collection<ITaskModel> tasks){
        if(jobId > 0) {
            for (ITaskModel taskModel : tasks) {
                if (!validateTaskModel(taskModel)) {
                    return;
                }
            }
        }
        ITaskModelRepository taskModelRepository = new TaskModelRepository();
        taskModelRepository.addTasksNeeded(jobId, tasks);
    }

    public static void addAll(int jobId, final Collection<ITaskModel> tasks){
        if(jobId > 0){
            for (ITaskModel taskModel: tasks){
                if(!validateTaskModel(taskModel)){
                    return;
                }
            }

            ITaskModelRepository taskModelRepository = new TaskModelRepository();
            taskModelRepository.addAll(tasks);
            taskModelRepository.addTasksNeeded(jobId, tasks);

        }
    }

    public static void addTaskName(final String taskName){
        if(validateTaskName(taskName)){
            ITaskModelRepository taskModelRepository = new TaskModelRepository();
            taskModelRepository.addTaskName(taskName);
        }
    }

    public static void update(final ITaskModel taskModel) {
        if(validateTaskModel(taskModel)){
            ITaskModelRepository taskModelRepository = new TaskModelRepository();
            taskModelRepository.update(taskModel);

        }
    }

    public static void updateAll(List<ITaskModel> tasks){
        ITaskModelRepository taskModelRepository = new TaskModelRepository();
        tasks.forEach(task ->{
            if(validateTaskModel(task)){
                taskModelRepository.update(task);
            }
        });
    }

    public static void updateTaskName(String oldTaskName, String newTaskName){
        if(validateTaskName(oldTaskName) && validateTaskName(newTaskName)){
            ITaskModelRepository taskModelRepository = new TaskModelRepository();
            taskModelRepository.updateTaskName(oldTaskName, newTaskName);
        }
    }

    public static void remove(final ITaskModel taskModel) {
        if(validateTaskModel(taskModel)){
            ITaskModelRepository taskModelRepository = new TaskModelRepository();
            taskModelRepository.remove(taskModel);
        }
    }

    public static void removeJobTask(int jobId, int taskId){
        if(jobId > 0 && taskId > 0){
            ITaskModelRepository taskModelRepository = new TaskModelRepository();
            taskModelRepository.removeJobTask(jobId, taskId);
        }
    }

    public static void removeTaskAssignedTechnician(int taskId, int technicianId){
        if(taskId > 0 && technicianId > 0){
            ITaskModelRepository taskModelRepository = new TaskModelRepository();
            taskModelRepository.removeTaskAssignedTechnician(taskId, technicianId);
        }
    }

    public ITaskModel getById(final int id){
        if(id > 0){
            ITaskModelRepository taskModelRepository = new TaskModelRepository();
            return taskModelRepository.getById(id);
        }

        return null;
    }

    public static List<ITaskModel> getAll() {
        ITaskModelRepository taskModelRepository = new TaskModelRepository();
        List<ITaskModel> tasks = taskModelRepository.getAll();

        for(ITaskModel task : tasks){
            if(!validateTaskModel(task)){
                System.out.println("Invalid task was loaded, when loading all parts.");
                return null;
            }
        }

        return tasks;
    }

    public static Set<String> getAllTaskNames() {
        ITaskModelRepository taskModelRepository = new TaskModelRepository();
        Set<String> taskNames = taskModelRepository.getAllTaskNames();


        return taskNames;
    }

    public static List<ITaskModel> getAllCompletedTasks(){
        ITaskModelRepository taskModelRepository = new TaskModelRepository();
        List<ITaskModel> tasks = taskModelRepository.getAllCompletedTasks();

        return tasks;
    }

    public static List<ITaskModel> getAllNotCompletedTasks(){
        ITaskModelRepository taskModelRepository = new TaskModelRepository();
        List<ITaskModel> tasks = taskModelRepository.getAllNotCompletedTasks();

        return tasks;
    }

    public static List<ITaskModel> getAllUnassignedTasks(){
        ITaskModelRepository taskModelRepository = new TaskModelRepository();
        List<ITaskModel> tasks = taskModelRepository.getAllUnassignedTasks();

        return tasks;
    }

    public static List<ITaskModel> getAllOverdueTasks(){
        ITaskModelRepository taskModelRepository = new TaskModelRepository();
        List<ITaskModel> tasks = taskModelRepository.getAllOverdueTasks();

        return tasks;
    }

    public static List<ITaskModel> getJobTasksNeeded(int jobId){
        ITaskModelRepository taskModelRepository = new TaskModelRepository();
        List<ITaskModel> tasks = taskModelRepository.getJobTasksNeeded(jobId);

        return tasks;
    }

    /**
     * Returns List of tasks that are currently assigned to an technician.
     * @param technicianId technicianId
     * @return list of tasks
     */
    public static List<ITaskModel> getTechniciansCurrentlyAssignedTasks(int technicianId){
        ITaskModelRepository taskModelRepository = new TaskModelRepository();
        List<ITaskModel> tasks = taskModelRepository.getTechniciansCurrentlyAssignedTasks(technicianId);

        return tasks;
    }

    private static boolean validateTaskName(String taskName){
        boolean isValid = true;

        if(taskName.isEmpty()){
            isValid = false;
            System.out.println("Error: Task Name cannot be null!");
        }

        if(taskName.length() > 128){
            System.out.println("Task name cannot have more than 128 characters");
            isValid = false;
        }

        return isValid;
    }

    private static boolean validateTaskModel(ITaskModel taskModel){
        boolean isValid = true;

        if(taskModel.getTaskName().length() > 128){
            System.out.println("Task name cannot have more than 128 characters");
            isValid = false;
        }

        if(taskModel.getTaskName().isEmpty()){
            System.out.println("Task name cannot be empty.");
            isValid = false;
        }

        if(taskModel.getTaskPriority().length() > 26){
            System.out.println("Task priority cannot have more than 26 characters.");
            isValid = false;
        }

        if(taskModel.getTaskPriority().isEmpty()){
            System.out.println("Task priority cannot be empty.");
            isValid = false;
        }

        if(taskModel.getTaskNotes().length() > 1028){
            System.out.println("Task notes cannot have more than 1028 characters.");
            isValid = false;
        }

        if(taskModel.getTaskPriority().length() > 26){
            System.out.println("Task priority cannot have more than 26 characters.");
            isValid = false;
        }

        if(taskModel.getHoursNeeded() <= 0){
            System.out.println("Hours needed must be 1 or higher.");
            isValid = false;
        }

        return isValid;
    }
}
