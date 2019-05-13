package com.plambeeco.dataaccess.dataprocessor;

import com.plambeeco.dataaccess.repository.IJobModelRepository;
import com.plambeeco.dataaccess.repository.JobModelRepository;
import com.plambeeco.models.JobModel;

import java.util.List;

public class JobModelProcessor {
    //TODO - Add proper validation
    private JobModelProcessor() {
        throw new RuntimeException("This class should not be initialized!");
    }
    public static void add(JobModel jobModel) {
        IJobModelRepository jobModelRepository = new JobModelRepository();
        jobModelRepository.add(jobModel);
    }


    public static void update(JobModel jobModel) {
        IJobModelRepository jobModelRepository = new JobModelRepository();
        jobModelRepository.update(jobModel);
        MotorModelProcessor.update(jobModel.getMotor());
        JobDetailsModelProcessor.update(jobModel.getJobDetails());
        TaskModelProcessor.updateAll(jobModel.getJobTasks());
        jobModel.getPartsNeeded().forEach(PartModelProcessor::update);
        jobModel.getJobTasks().forEach(TaskModelProcessor::update);
    }

    public static void remove(JobModel jobModel) {
        IJobModelRepository jobModelRepository = new JobModelRepository();
        jobModelRepository.remove(jobModel);
    }


    public static JobModel getById(int id) {
        IJobModelRepository jobModelRepository = new JobModelRepository();
        return jobModelRepository.getById(id);
    }


    public static List<JobModel> getAll() {
        IJobModelRepository jobModelRepository = new JobModelRepository();
        return jobModelRepository.getAll();
    }
}
