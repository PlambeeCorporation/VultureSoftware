package com.plambeeco.dataaccess.repository;

import com.plambeeco.dataaccess.dataprocessor.*;
import com.plambeeco.models.ITechnicianModel;
import com.plambeeco.models.JobModel;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JobModelRepository implements IJobModelRepository {
    private static final String CONNECTION_STRING = "jdbc:sqlite:vulture.sqlite";
    private static final String TABLE_NAME = "Job";
    private static final String ID_COLUMN = "Id";
    private static final String MOTOR_ID_COLUMN = "MotorId";
    private static final String JOB_DETAILS_ID_COLUMN = "JobDetailsId";
    private static final String INSPECTING_TECHNICIAN_ID_COLUMN = "InspectingTechnicianId";
    private static final String INSPECTION_DATE_COLUMN = "InspectionDate";
    private static final String JOB_APPROVED_COLUMN = "JobApproved";

    @Override
    public void add(JobModel jobModel) {
        final String sql =
                "INSERT INTO " + TABLE_NAME + "(" + MOTOR_ID_COLUMN + "," + JOB_DETAILS_ID_COLUMN  + "," +
                        INSPECTING_TECHNICIAN_ID_COLUMN + ", " + INSPECTION_DATE_COLUMN + ", " + JOB_APPROVED_COLUMN + ")" +
                        "VALUES(?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setInt(1, jobModel.getMotor().getMotorId());
            ps.setInt(2, jobModel.getJobDetails().getJobDetailsId());
            ps.setNull(3, Types.INTEGER);
            ps.setNull(4, Types.DATE);
            ps.setBoolean(5, jobModel.isJobApproved());


            int affectedRows = ps.executeUpdate();

            if(affectedRows == 0){
                throw new SQLException("Adding job failed, no rows affected");
            }

            try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                if(generatedKeys.next()){
                    jobModel.setJobId(generatedKeys.getInt(1));
                }else{
                    throw new SQLException("Adding job failed, no ID obtained");
                }
            }

        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void update(JobModel jobModel) {
        final String sql =
                "Update " + TABLE_NAME +
                        " SET( " +  MOTOR_ID_COLUMN + "," + JOB_DETAILS_ID_COLUMN + ", " +
                        INSPECTING_TECHNICIAN_ID_COLUMN + ", " + INSPECTION_DATE_COLUMN + ", " + JOB_APPROVED_COLUMN +
                        ")=" + "(?,?,?,?,?)" +
                        " WHERE " +
                        ID_COLUMN + " =?";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, jobModel.getMotor().getMotorId());
            ps.setInt(2, jobModel.getJobDetails().getJobDetailsId());
            ps.setInt(3, jobModel.getInspectingTechnician().getPersonId());
            ps.setDate(4, Date.valueOf(jobModel.getInspectionDate()));
            ps.setBoolean(5, jobModel.isJobApproved());
            ps.setInt(6, jobModel.getJobId());

            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to update the Job: " + e.getMessage());
        }
    }

    @Override
    public void remove(JobModel jobModel) {
        final String sql = "DELETE FROM " + TABLE_NAME + " WHERE Id=? ";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, jobModel.getJobId());

            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to delete job from the database: " + e.getMessage());
        }
    }

    @Override
    public JobModel getById(int id) {
        final String sql =
                "SELECT *"  +
                        " FROM " + TABLE_NAME +
                        " WHERE " + ID_COLUMN + "= ?";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new JobModel.JobBuilder()
                            .setJobId(rs.getInt(ID_COLUMN))
                            .setMotor(MotorModelProcessor.getById(rs.getInt(MOTOR_ID_COLUMN)))
                            .setJobDetails(JobDetailsModelProcessor.getById(rs.getInt(JOB_DETAILS_ID_COLUMN)))
                            .setPartsNeeded(PartModelProcessor.getJobPartsNeeded(rs.getInt(ID_COLUMN)))
                            .setJobTasks(TaskModelProcessor.getJobTasksNeeded(rs.getInt(ID_COLUMN)))
                            .setInspectingTechnician((ITechnicianModel)PersonModelProcessor.getById(rs.getInt(INSPECTING_TECHNICIAN_ID_COLUMN)))
                            .setInspectionDate(rs.getDate(INSPECTION_DATE_COLUMN).toLocalDate())
                            .setJobApproved(rs.getBoolean(JOB_APPROVED_COLUMN))
                            .build();
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve job from the database: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<JobModel> getAll() {
        List<JobModel> jobs = new ArrayList<>();

        final String sql =
                "SELECT *"  +
                        " FROM " + TABLE_NAME;

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    ITechnicianModel inspectingTechnician = null;
                    LocalDate inspectionDate = null;

                    if(rs.getInt(INSPECTING_TECHNICIAN_ID_COLUMN) > 0){
                        inspectingTechnician = PersonModelProcessor.getTechnicianById(rs.getInt(INSPECTING_TECHNICIAN_ID_COLUMN));
                    }

                    if(rs.getDate(INSPECTION_DATE_COLUMN).toLocalDate() != null){
                        inspectionDate = rs.getDate(INSPECTION_DATE_COLUMN).toLocalDate();
                    }


                      JobModel job = new JobModel.JobBuilder()
                            .setJobId(rs.getInt(ID_COLUMN))
                            .setMotor(MotorModelProcessor.getById(rs.getInt(MOTOR_ID_COLUMN)))
                            .setJobDetails(JobDetailsModelProcessor.getById(rs.getInt(JOB_DETAILS_ID_COLUMN)))
                            .setPartsNeeded(PartModelProcessor.getJobPartsNeeded(rs.getInt(ID_COLUMN)))
                            .setJobTasks(TaskModelProcessor.getJobTasksNeeded(rs.getInt(ID_COLUMN)))
                            .setInspectingTechnician(inspectingTechnician)
                            .setInspectionDate(inspectionDate)
                            .setJobApproved(rs.getBoolean(JOB_APPROVED_COLUMN))
                            .build();
                      jobs.add(job);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve job from the database: " + e.getMessage());
        }

        return jobs;
    }

    @Override
    public List<JobModel> getAllFinishedJobs() {
        List<JobModel> jobs = new ArrayList<>();

        final String sql =
                "SELECT *"  +
                    " FROM " + TABLE_NAME +
                    " WHERE " + JOB_APPROVED_COLUMN + " == True";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    JobModel job = new JobModel.JobBuilder()
                            .setJobId(rs.getInt(ID_COLUMN))
                            .setMotor(MotorModelProcessor.getById(rs.getInt(MOTOR_ID_COLUMN)))
                            .setJobDetails(JobDetailsModelProcessor.getById(rs.getInt(JOB_DETAILS_ID_COLUMN)))
                            .setPartsNeeded(PartModelProcessor.getJobPartsNeeded(rs.getInt(ID_COLUMN)))
                            .setJobTasks(TaskModelProcessor.getJobTasksNeeded(rs.getInt(ID_COLUMN)))
                            .setInspectingTechnician((ITechnicianModel)PersonModelProcessor.getById(rs.getInt(INSPECTING_TECHNICIAN_ID_COLUMN)))
                            .setInspectionDate(rs.getDate(INSPECTION_DATE_COLUMN).toLocalDate())
                            .setJobApproved(rs.getBoolean(JOB_APPROVED_COLUMN))
                            .build();
                    jobs.add(job);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve job from the database: " + e.getMessage());
        }

        return jobs;
    }

    @Override
    public List<JobModel> getAllUnfinishedJobs() {
        List<JobModel> jobs = new ArrayList<>();

        final String sql =
                "SELECT *"  +
                        " FROM " + TABLE_NAME +
                        " WHERE " + JOB_APPROVED_COLUMN + " == FALSE";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    JobModel job = new JobModel.JobBuilder()
                            .setJobId(rs.getInt(ID_COLUMN))
                            .setMotor(MotorModelProcessor.getById(rs.getInt(MOTOR_ID_COLUMN)))
                            .setJobDetails(JobDetailsModelProcessor.getById(rs.getInt(JOB_DETAILS_ID_COLUMN)))
                            .setPartsNeeded(PartModelProcessor.getJobPartsNeeded(rs.getInt(ID_COLUMN)))
                            .setJobTasks(TaskModelProcessor.getJobTasksNeeded(rs.getInt(ID_COLUMN)))
                            .setInspectingTechnician((ITechnicianModel)PersonModelProcessor.getById(rs.getInt(INSPECTING_TECHNICIAN_ID_COLUMN)))
                            .setInspectionDate(rs.getDate(INSPECTION_DATE_COLUMN).toLocalDate())
                            .setJobApproved(rs.getBoolean(JOB_APPROVED_COLUMN))
                            .build();
                    jobs.add(job);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve job from the database: " + e.getMessage());
        }

        return jobs;

    }

    @Override
    public List<JobModel> getJobsByDate(LocalDate from, LocalDate to) {
        return null;
    }
}
