package com.plambeeco.dataaccess.repository;

import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.ITechnicianModel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ITaskModelRepository extends IRepository<ITaskModel> {
    void addTasksNeeded(int jobId, final Collection<ITaskModel> tasks);
    void addTaskAssignedTechnician(ITaskModel task, int technicianId);
    void addAll(Collection<ITaskModel> tasks);
    void addTaskName(String taskName);
    void updateTaskName(String oldTaskName, String newTaskName);
    void removeJobTask(int jobId, int taskId);
    void removeTaskAssignedTechnician(int taskId, int technicianId);
    List<ITaskModel> getAllCompletedTasks();
    List<ITaskModel> getAllNotCompletedTasks();
    Set<String> getAllTaskNames();
    List<ITaskModel> getJobTasksNeeded(int jobId);
    List<ITaskModel> getTechniciansCurrentlyAssignedTasks(int technicianId);
}
