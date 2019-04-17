package com.plambeeco.dataaccess.dataprocessor;

import com.plambeeco.dataaccess.repository.IJobDetailsModelRepository;
import com.plambeeco.dataaccess.repository.JobDetailsModelRepository;
import com.plambeeco.models.IJobDetailsModel;

import java.util.List;
//TODO - Add proper validation
public class JobDetailsModelProcessor {
    private JobDetailsModelProcessor() {
        throw new RuntimeException("This class should not be initialized!");
    }
    public static void add(IJobDetailsModel jobDetailsModel) {
        IJobDetailsModelRepository jobDetailsRepository = new JobDetailsModelRepository();
        jobDetailsRepository.add(jobDetailsModel);
    }


    public static void update(IJobDetailsModel jobDetailsModel) {
        IJobDetailsModelRepository jobDetailsRepository = new JobDetailsModelRepository();
        jobDetailsRepository.update(jobDetailsModel);
    }

    public static void remove(IJobDetailsModel jobDetailsModel) {
        IJobDetailsModelRepository jobDetailsRepository = new JobDetailsModelRepository();
        jobDetailsRepository.remove(jobDetailsModel);
    }

    public static IJobDetailsModel getById(int id) {
        IJobDetailsModelRepository jobDetailsRepository = new JobDetailsModelRepository();
        return jobDetailsRepository.getById(id);
    }

    public static List<IJobDetailsModel> getAll() {
        IJobDetailsModelRepository jobDetailsRepository = new JobDetailsModelRepository();
        return jobDetailsRepository.getAll();
    }
}
