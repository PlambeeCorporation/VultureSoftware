package com.plambeeco.dataaccess.dataprocessor;

import com.plambeeco.dataaccess.repository.IJobModelRepository;
import com.plambeeco.dataaccess.repository.JobModelRepository;
import com.plambeeco.models.JobDetailsModel;
import com.plambeeco.models.JobModel;
import com.plambeeco.models.PartModel;

import java.time.LocalDate;
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


    public static List<JobModel> getAllFinishedJobs() {
        IJobModelRepository jobModelRepository = new JobModelRepository();
        return jobModelRepository.getAllFinishedJobs();
    }


    public static List<JobModel> getAllUnfinishedJobs() {
        IJobModelRepository jobModelRepository = new JobModelRepository();
        return jobModelRepository.getAllUnfinishedJobs();
    }


    public static List<JobModel> getJobsByDate(LocalDate from, LocalDate to) {
        IJobModelRepository jobModelRepository = new JobModelRepository();
        return jobModelRepository.getJobsByDate(from, to);
    }
}
