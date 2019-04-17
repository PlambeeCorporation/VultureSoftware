package com.plambeeco.dataaccess.repository;

import com.plambeeco.models.IMotorModel;
import com.plambeeco.models.MotorModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MotorModelRepository implements IMotorModelRepository {
    private static final String CONNECTION_STRING = "jdbc:sqlite:vulture.sqlite";
    private static final String TABLE_NAME = "MotorDetails";
    private static final String ID_COLUMN = "Id";
    private static final String MOTOR_TYPE_COLUMN = "MotorType";
    private static final String MANUFACTURER_COLUMN = "Manufacturer";
    private static final String ESTIMATED_YEAR_OF_MANUFACTURER_COLUMN = "EstimatedYearOfManufacture";

    /**
     * Adds motor model to the database.
     * @param motorModel Motor Model to add.
     */
    @Override
    public void add(IMotorModel motorModel) {
        final String sql =
                "INSERT INTO  " + TABLE_NAME +
                "(" + MOTOR_TYPE_COLUMN + "," + MANUFACTURER_COLUMN + "," + ESTIMATED_YEAR_OF_MANUFACTURER_COLUMN + ")" +
                "VALUES(?, ?, ?)";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, motorModel.getMotorType());
            ps.setString(2, motorModel.getManufacturer());
            ps.setInt(3, motorModel.getEstimatedYearOfManufacture());

            int affectedRows = ps.executeUpdate();

            if(affectedRows == 0){
                throw new SQLException("Adding motor details failed, no rows affected");
            }

            try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                if(generatedKeys.next()){
                    motorModel.setMotorId(generatedKeys.getInt(1));
                }else{
                    throw new SQLException("Adding motor details failed, no ID obtained");
                }
            }

        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    /**
     * Updates motor model in the database.
     * @param motorModel Updated Motor Model.
     */
    @Override
    public void update(IMotorModel motorModel) {
        String sql =
                "Update " + TABLE_NAME +
                " SET( " + MOTOR_TYPE_COLUMN + ", " + MANUFACTURER_COLUMN + ", " + ESTIMATED_YEAR_OF_MANUFACTURER_COLUMN +
                ")=" + "(?,?,?)" +
                " WHERE " +
                ID_COLUMN + " =?;";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, motorModel.getMotorType());
            ps.setString(2, motorModel.getManufacturer());
            ps.setInt(3, motorModel.getEstimatedYearOfManufacture());
            ps.setInt(4, motorModel.getMotorId());

            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to update the motor details: " + e.getMessage());
        }
    }

    /**
     * Removes motor model from the database.
     * @param motorModel Motor Model to remove.
     */
    @Override
    public void remove(IMotorModel motorModel) {
        final String sql =
                "DELETE FROM " + TABLE_NAME +
                " WHERE " + ID_COLUMN + "=? ";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, motorModel.getMotorId());

            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to delete motor details from the database: " + e.getMessage());
        }
    }

    /**
     * Returns motor model with matching Id.
     * @param id    Motor's model unique identifier.
     * @return  Motor model.
     */
    @Override
    public IMotorModel getById(int id) {
        final String sql =
                "SELECT *"  +
                        " FROM " + TABLE_NAME +
                        " WHERE " + ID_COLUMN + "= ?";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    IMotorModel motorModel = new MotorModel(rs.getString(MOTOR_TYPE_COLUMN),
                                                            rs.getString(MANUFACTURER_COLUMN),
                                                            rs.getInt(ESTIMATED_YEAR_OF_MANUFACTURER_COLUMN));
                    motorModel.setMotorId(rs.getInt(ID_COLUMN));
                    return motorModel;
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve motor details from the database: " + e.getMessage());
        }
        return null;
    }

    /**
     * Returns all Motor models in the database.
     * @return list of motor models.
     */
    @Override
    public List<IMotorModel> getAll() {
        List<IMotorModel> motors = new ArrayList<>();
        final String sql =
                "SELECT * FROM " + TABLE_NAME;


        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    IMotorModel motorModel = new MotorModel(rs.getString(MOTOR_TYPE_COLUMN), rs.getString(MANUFACTURER_COLUMN),
                            rs.getInt(ESTIMATED_YEAR_OF_MANUFACTURER_COLUMN));
                    motorModel.setMotorId(rs.getInt(ID_COLUMN));
                    motors.add(motorModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve all motor details from the database: " + e.getMessage());
        }

        return motors;
    }
}
