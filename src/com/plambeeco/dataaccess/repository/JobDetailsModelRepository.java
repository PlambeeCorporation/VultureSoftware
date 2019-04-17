package com.plambeeco.dataaccess.repository;

import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.models.IJobDetailsModel;
import com.plambeeco.models.ITechnicianModel;
import com.plambeeco.models.JobDetailsModel;
import com.plambeeco.models.TechnicianModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDetailsModelRepository implements IJobDetailsModelRepository {
    private static final String CONNECTION_STRING = "jdbc:sqlite:vulture.sqlite";
    private static final String TABLE_NAME = "JobDetails";
    private static final String ID_COLUMN = "Id";
    private static final String CLIENT_ID_COLUMN = "ClientId";
    private static final String CHECK_BY_TECHNICIAN_ID_COLUMN = "CheckBy_TechnicianId";
    private static final String CHECKING_DATE_COLUMN = "CheckingDate";
    private static final String DATE_COLLECTED_COLUMN = "DateCollected";
    private static final String ESTIMATED_LABOUR_TIME_COLUMN = "EstimatedLabourTime";
    private static final String RETURN_DATE_COLUMN = "ReturnDate";

    @Override
    public void add(IJobDetailsModel jobDetails) {
        final String sql =
                "INSERT INTO " + TABLE_NAME + "(" + CLIENT_ID_COLUMN + "," + CHECK_BY_TECHNICIAN_ID_COLUMN + ", " +
                        CHECKING_DATE_COLUMN + ", " + DATE_COLLECTED_COLUMN +  ", " + ESTIMATED_LABOUR_TIME_COLUMN +
                        ", " + RETURN_DATE_COLUMN + ") " +
                        "VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setInt(1, jobDetails.getClient().getPersonId());
            ps.setInt(2, jobDetails.getCheckedBy_Technician().getPersonId());
            ps.setDate(3, Date.valueOf(jobDetails.getCheckingDate()));
            ps.setDate(4, Date.valueOf(jobDetails.getDateCollected()));
            ps.setFloat(5, jobDetails.getEstimatedLabourTime());
            ps.setDate(6, Date.valueOf(jobDetails.getReturnDate()));

            int affectedRows = ps.executeUpdate();

            if(affectedRows == 0){
                throw new SQLException("Adding Job Details failed, no rows affected");
            }

            try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                if(generatedKeys.next()){
                    jobDetails.setJobDetailsId(generatedKeys.getInt(1));
                }else{
                    throw new SQLException("Adding Job Details failed, no ID obtained");
                }
            }

        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void update(IJobDetailsModel jobDetails) {
        final String sql =
                "Update " + TABLE_NAME +
                        " SET( " + CLIENT_ID_COLUMN + ", " + CHECK_BY_TECHNICIAN_ID_COLUMN + ", " +
                        CHECKING_DATE_COLUMN + ", " + DATE_COLLECTED_COLUMN + ESTIMATED_LABOUR_TIME_COLUMN +
                        RETURN_DATE_COLUMN +
                        ")=" + "(?,?,?,?,?,?)" +
                        " WHERE " +
                        ID_COLUMN + " =?;";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, jobDetails.getClient().getPersonId());
            ps.setInt(2, jobDetails.getCheckedBy_Technician().getPersonId());
            ps.setDate(3, Date.valueOf(jobDetails.getCheckingDate()));
            ps.setDate(4, Date.valueOf(jobDetails.getDateCollected()));
            ps.setFloat(5, jobDetails.getEstimatedLabourTime());
            ps.setDate(6, Date.valueOf(jobDetails.getReturnDate()));
            ps.setInt(6, jobDetails.getJobDetailsId());

            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to update the Job Details: " + e.getMessage());
        }
    }

    @Override
    public void remove(IJobDetailsModel jobDetails) {
        final String sql =
                "DELETE FROM " + TABLE_NAME +
                        " WHERE " + ID_COLUMN + "=? ";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, jobDetails.getJobDetailsId());

            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to delete job details from the database: " + e.getMessage());
        }
    }

    @Override
    public IJobDetailsModel getById(int id) {
        final String sql =
                "SELECT *"  +
                        " FROM " + TABLE_NAME +
                        " WHERE " + ID_COLUMN + "= ?";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    ITechnicianModel inspectingTechnician;
                    if(rs.getInt(CHECK_BY_TECHNICIAN_ID_COLUMN) > 0){
                        inspectingTechnician = PersonModelProcessor.getTechnicianById(rs.getInt(CHECK_BY_TECHNICIAN_ID_COLUMN));
                    }else{
                        inspectingTechnician = null;
                    }

                    IJobDetailsModel jobDetailsModel = new JobDetailsModel(
                            PersonModelProcessor.getById(rs.getInt(CLIENT_ID_COLUMN)),
                            inspectingTechnician,
                            rs.getDate(CHECKING_DATE_COLUMN).toLocalDate(),
                            rs.getDate(DATE_COLLECTED_COLUMN).toLocalDate(),
                            rs.getInt(ESTIMATED_LABOUR_TIME_COLUMN),
                            rs.getDate(RETURN_DATE_COLUMN).toLocalDate());


                    jobDetailsModel.setJobDetailsId(rs.getInt(ID_COLUMN));

                    return jobDetailsModel;
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve motor details from the database: " + e.getMessage());
        }
        return null;

    }

    @Override
    public List<IJobDetailsModel> getAll() {
        List<IJobDetailsModel> jobDetails = new ArrayList<>();
        final String sql =
                        "SELECT * " +
                        "FROM " +
                        TABLE_NAME;

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
//                    ITechnicianModel inspectingTechnician;
//                    if(rs.getInt(CHECK_BY_TECHNICIAN_ID_COLUMN) > 0){
//                        inspectingTechnician = ;
//                    }else{
//                        inspectingTechnician = null;
//                    }

                    IJobDetailsModel jobDetail = new JobDetailsModel(
                             PersonModelProcessor.getById(rs.getInt(CLIENT_ID_COLUMN)),
                             PersonModelProcessor.getTechnicianById(rs.getInt(CHECK_BY_TECHNICIAN_ID_COLUMN)),
                             rs.getDate(CHECKING_DATE_COLUMN).toLocalDate(),
                             rs.getDate(DATE_COLLECTED_COLUMN).toLocalDate(),
                             rs.getInt(ESTIMATED_LABOUR_TIME_COLUMN),
                             rs.getDate(RETURN_DATE_COLUMN).toLocalDate());

                    jobDetail.setJobDetailsId(rs.getInt(ID_COLUMN));
                    jobDetails.add(jobDetail);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve from the database: " + e.getMessage());
        }

        return jobDetails;
    }
}
