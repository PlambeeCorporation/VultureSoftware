package com.plambeeco.dataaccess.repository;

import com.plambeeco.VultureApplication;
import com.plambeeco.dataaccess.dataprocessor.TaskModelProcessor;
import com.plambeeco.helper.AlertHelper;
import com.plambeeco.helper.ConstantValuesHelper;
import com.plambeeco.models.IPersonModel;
import com.plambeeco.models.ITechnicianModel;
import com.plambeeco.models.PersonModel;
import com.plambeeco.models.TechnicianModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonModelRepository implements IPersonModelRepository {
    private static final String TABLE_NAME = "People";
    private static final String ID_COLUMN = "Id";
    private static final String FORENAME_COLUMN = "Forename";
    private static final String SURNAME_COLUMN = "Surname";
    private static final String EMAIL_ADDRESS_COLUMN = "EmailAddress";
    private static final String PHONE_NUMBER_COLUMN = "PhoneNumber";

    private static final String ACCOUNT_TABLE_NAME = "Account";
    private static final String ACCOUNT_PEOPLE_FOREIGN_KEY_COLUMN = "AccountOwnerId";
    private static final String ACCOUNT_TYPE_COLUMN = "AccountType";

    private static final String TASK_TABLE = "Tasks";
    private static final String TASK_COMPLETED = "TaskCompleted";

    private static final String TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME = "TechnicianAssignedTasks";
    private static final String TASK_ID = "TaskId";
    private static final String TECHNICIAN_ID = "TechnicianId";


    @Override
    public void add(IPersonModel personModel) {
        final String sql =
                "INSERT INTO  " + TABLE_NAME +
                        "(" + FORENAME_COLUMN + "," + SURNAME_COLUMN + "," + EMAIL_ADDRESS_COLUMN + "," +
                        PHONE_NUMBER_COLUMN + ")" +
                        "VALUES(?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, personModel.getForename());
            ps.setString(2, personModel.getSurname());
            ps.setString(3, personModel.getEmailAddress());
            ps.setString(4, personModel.getPhoneNumber());

            int affectedRows = ps.executeUpdate();

            if(affectedRows == 0){
                throw new SQLException("Adding new Person failed, no rows affected");
            }

            try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                if(generatedKeys.next()){
                    personModel.setPersonId(generatedKeys.getInt(1));
                }else{
                    throw new SQLException("Adding new Person failed, no ID obtained");
                }
            }

        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void update(IPersonModel personModel) {
        String sql =
                "Update " + TABLE_NAME +
                        " SET( " + FORENAME_COLUMN + ", " + SURNAME_COLUMN + ", " + EMAIL_ADDRESS_COLUMN +
                        ", " + PHONE_NUMBER_COLUMN +
                        ")=" + "(?,?,?,?)" +
                        " WHERE " +
                        ID_COLUMN + " =?;";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, personModel.getForename());
            ps.setString(2, personModel.getSurname());
            ps.setString(3, personModel.getEmailAddress());
            ps.setString(4, personModel.getPhoneNumber());
            ps.setInt(5, personModel.getPersonId());

            ps.execute();
            AlertHelper.showAlert(VultureApplication.getPrimaryStage(), "Personal details updated",
                    "Personal details have been updated");
        }catch(SQLException e){
            System.out.println("Failed to update the person details: " + e.getMessage());
        }
    }

    @Override
    public void remove(IPersonModel personModel) {
        final String sql =
                "DELETE FROM " + TABLE_NAME +
                        " WHERE " + ID_COLUMN + "=? ";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, personModel.getPersonId());

            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to delete person from the database: " + e.getMessage());
        }
    }

    @Override
    public IPersonModel getById(int id) {
        final String sql =
                "SELECT " + ID_COLUMN + ", " + FORENAME_COLUMN + ", " + SURNAME_COLUMN + ", " + EMAIL_ADDRESS_COLUMN +
                        ", " + PHONE_NUMBER_COLUMN +
                        " FROM " + TABLE_NAME +
                        " WHERE " + ID_COLUMN + "=?";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                   IPersonModel personModel = new PersonModel(rs.getString(FORENAME_COLUMN),
                                                              rs.getString(SURNAME_COLUMN),
                                                              rs.getString(EMAIL_ADDRESS_COLUMN),
                                                              rs.getString(PHONE_NUMBER_COLUMN));
                   personModel.setPersonId(rs.getInt(ID_COLUMN));
                   return personModel;
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve person details from the database: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<IPersonModel> getAll() {
        List<IPersonModel> people = new ArrayList<>();
        final String sql =
                "SELECT * FROM " + TABLE_NAME;


        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    IPersonModel personModel = new PersonModel(rs.getString(FORENAME_COLUMN),
                            rs.getString(SURNAME_COLUMN),
                            rs.getString(EMAIL_ADDRESS_COLUMN),
                            rs.getString(PHONE_NUMBER_COLUMN));
                    personModel.setPersonId(rs.getInt(ID_COLUMN));
                    people.add(personModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve all people from the database: " + e.getMessage());
        }

        return people;
    }



    @Override
    public List<ITechnicianModel> getAllTechnicians() {
        List<ITechnicianModel> technicians = new ArrayList<>();

        final String sql =
                "SELECT " + TABLE_NAME + ".* " + "FROM "  + TABLE_NAME +
                        " LEFT JOIN " + ACCOUNT_TABLE_NAME + " ON " + TABLE_NAME + "." + ID_COLUMN + " = " +
                        ACCOUNT_TABLE_NAME + "." + ACCOUNT_PEOPLE_FOREIGN_KEY_COLUMN +
                        " WHERE " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_TYPE_COLUMN + " = " + "\"Technician\"";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    ITechnicianModel technicianModel = new TechnicianModel(rs.getString(FORENAME_COLUMN),
                            rs.getString(SURNAME_COLUMN),
                            rs.getString(EMAIL_ADDRESS_COLUMN),
                            rs.getString(PHONE_NUMBER_COLUMN),
                            TaskModelProcessor.getTechniciansCurrentlyAssignedTasks(rs.getInt(ID_COLUMN)));
                    technicianModel.setPersonId(rs.getInt(ID_COLUMN));
                    technicians.add(technicianModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve all technicians from the database: " + e.getMessage());
        }

        return technicians;
    }

    @Override
    public List<IPersonModel> getAllClients() {
        List<IPersonModel> clients = new ArrayList<>();

        final String sql =
                "SELECT " + TABLE_NAME + ".* " + "FROM "  + TABLE_NAME +
                        " LEFT JOIN " + ACCOUNT_TABLE_NAME + " ON " + TABLE_NAME + "." + ID_COLUMN + " = " +
                        ACCOUNT_TABLE_NAME + "." + ACCOUNT_PEOPLE_FOREIGN_KEY_COLUMN +
                        " WHERE " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_TYPE_COLUMN + " = " + "\"Client\"";


        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    IPersonModel client = new PersonModel(rs.getString(FORENAME_COLUMN),
                            rs.getString(SURNAME_COLUMN),
                            rs.getString(EMAIL_ADDRESS_COLUMN),
                            rs.getString(PHONE_NUMBER_COLUMN));
                    client.setPersonId(rs.getInt(ID_COLUMN));
                    clients.add(client);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve all clients from the database: " + e.getMessage());
        }

        return clients;
    }

    @Override
    public List<ITechnicianModel> getAssignedTechnicians(int taskId) {
        List<ITechnicianModel> assignedTechnicians = new ArrayList<>();

        final String sql =
                "SELECT " + TABLE_NAME +  ".* "  +
                        " FROM " + TABLE_NAME +
                        " LEFT JOIN " + TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME + " ON " +
                        TABLE_NAME + "." + ID_COLUMN + " = " +
                        TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME + "." + TECHNICIAN_ID +
                        " WHERE " + TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME + "." + TASK_ID +
                        " = ?";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, taskId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    ITechnicianModel technicianModel = new TechnicianModel(rs.getString(FORENAME_COLUMN),
                            rs.getString(SURNAME_COLUMN),
                            rs.getString(EMAIL_ADDRESS_COLUMN),
                            rs.getString(PHONE_NUMBER_COLUMN),
                            new ArrayList<>(TaskModelProcessor.getTechniciansCurrentlyAssignedTasks(rs.getInt(ID_COLUMN))));
                    technicianModel.setPersonId(rs.getInt(ID_COLUMN));
                    assignedTechnicians.add(technicianModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve the technicians from the database: " + e.getMessage());
        }
        return assignedTechnicians;
    }
}
