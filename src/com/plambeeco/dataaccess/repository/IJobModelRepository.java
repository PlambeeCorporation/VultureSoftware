package com.plambeeco.dataaccess.repository;

import com.plambeeco.models.JobModel;

import java.time.LocalDate;
import java.util.List;

public interface IJobModelRepository extends IRepository<JobModel> {
    List<JobModel> getAllFinishedJobs();
    List<JobModel> getAllUnfinishedJobs();
    List<JobModel> getJobsByDate(LocalDate from, LocalDate to);
}
