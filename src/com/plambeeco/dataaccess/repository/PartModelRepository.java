package com.plambeeco.dataaccess.repository;

import com.plambeeco.models.IPartModel;
import com.plambeeco.models.PartModel;

import java.sql.*;
import java.util.*;

public class PartModelRepository implements IPartModelRepository {
    private static final String CONNECTION_STRING = "jdbc:sqlite:vulture.sqlite";
    private static final String TABLE_NAME = "Parts";
    private static final String ID_COLUMN = "Id";
    private static final String PART_NAME_COLUMN = "PartName";
    private static final String QUANTITY_COLUMN = "Quantity";

    private static final String PARTS_NEEDED_TABLE_NAME = "JobPartsNeeded";
    private static final String JOB_ID_COLUMN = "JobId";
    private static final String PartNeeded_ID_COLUMN = "PartId";

    /**
     * Adds new
     * @param partModel
     */
    @Override
    public void add(final IPartModel partModel) {
        final String sql =
                "INSERT INTO " + TABLE_NAME + "(" + PART_NAME_COLUMN + "," + QUANTITY_COLUMN + ") " +
                "VALUES(?, ?)";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

             ps.setString(1, partModel.getPartName());
             ps.setInt(2, partModel.getPartQuantity());
             int affectedRows = ps.executeUpdate();

             if(affectedRows == 0){
                 throw new SQLException("Adding part failed, no rows affected");
             }

             try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                 if(generatedKeys.next()){
                     partModel.setPartId(generatedKeys.getInt(1));
                 }else{
                     throw new SQLException("Adding part failed, no ID obtained");
                 }
             }

        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void addPartsNeeded(int jobId, final Collection<IPartModel> parts) {
        final String sql =
                "INSERT INTO " + PARTS_NEEDED_TABLE_NAME + "(" + JOB_ID_COLUMN + "," + PartNeeded_ID_COLUMN + ")" +
                "VALUES(?,?)";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
             ps.setInt(1, jobId);
            for (IPartModel partModel : parts){
                ps.setInt(2, partModel.getPartId());
                int affectedRows = ps.executeUpdate();

                if(affectedRows == 0){
                    throw new SQLException("Adding part failed, no rows affected");
                }
            }

        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }


    @Override
    public void addAll(final Collection<IPartModel> parts){
        final String sql =
                "INSERT INTO " + TABLE_NAME + "(" + PART_NAME_COLUMN + "," + QUANTITY_COLUMN + ") " +
                "VALUES(?, ?)";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            for (IPartModel partModel : parts){
                ps.setString(1, partModel.getPartName());
                ps.setInt(2, partModel.getPartQuantity());
                int affectedRows = ps.executeUpdate();

                if(affectedRows == 0){
                    throw new SQLException("Adding part failed, no rows affected");
                }

                try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        partModel.setPartId(generatedKeys.getInt(1));
                    }else{
                        throw new SQLException("Adding part failed, no ID obtained");
                    }
                }
            }

        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void addPartName(String partName){
        final String sql =
                "INSERT INTO " + TABLE_NAME + "(" + PART_NAME_COLUMN + ")"  +
                        "VALUES(?)";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, partName);
            int affectedRows = ps.executeUpdate();

            if(affectedRows == 0){
                throw new SQLException("Adding part name failed, no rows affected");
            }
        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void update(final IPartModel partModel) {
        final String sql =
                "Update " + TABLE_NAME +
                        " SET( " + PART_NAME_COLUMN + ", " + QUANTITY_COLUMN +
                        ")=" + "(?,?)" +
                        " WHERE " +
                        ID_COLUMN + " =?;";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
                ps.setString(1, partModel.getPartName());
                ps.setInt(2, partModel.getPartQuantity());
                ps.setInt(3, partModel.getPartId());

                ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to update the Part: " + e.getMessage());
        }
    }

    @Override
    public void updatePartName(String oldPartName, String newPartName){
        final String sql =
                "Update " + TABLE_NAME +
                        " SET( " + PART_NAME_COLUMN +
                        ")=" + "(?)" +
                        " WHERE " +
                        PART_NAME_COLUMN + " =?;";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, newPartName);
            ps.setString(2, oldPartName);

            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to update the Part Name: " + e.getMessage());
        }
    }

    @Override
    public void remove(final IPartModel partModel) {
        final String sql = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + " =?";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, partModel.getPartId());

            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to delete part from the database: " + e.getMessage());
        }
    }

    @Override
    public IPartModel getById(final int id) {
        final String sql =
                "SELECT *"  +
                        " FROM " + TABLE_NAME +
                        " WHERE " + ID_COLUMN + "= ?";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    IPartModel partModel = new PartModel(rs.getString(PART_NAME_COLUMN), rs.getInt(QUANTITY_COLUMN));
                    partModel.setPartId(rs.getInt(ID_COLUMN));
                    return partModel;
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve from the database: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<IPartModel> getAll() {
        List<IPartModel> parts = new ArrayList<>();
        final String sql =
                "SELECT * " +
                "FROM " +
                TABLE_NAME +
                " WHERE " + QUANTITY_COLUMN + " != NULL";


        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    IPartModel partModel = new PartModel(rs.getString(PART_NAME_COLUMN),
                                                        rs.getInt(QUANTITY_COLUMN));
                    partModel.setPartId(rs.getInt(ID_COLUMN));
                    parts.add(partModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve all the parts from the database: " + e.getMessage());
        }

        return parts;
    }

    @Override
    public Set<String> getAllPartNames() {
        Set<String> partNames = new HashSet<>();

        final String sql =
                "SELECT DISTINCT " + PART_NAME_COLUMN +
                " FROM " + TABLE_NAME;

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    partNames.add(rs.getString(PART_NAME_COLUMN));
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve the part names from the database: " + e.getMessage());
        }

        return partNames;
    }

    @Override
    public List<IPartModel> getJobPartsNeeded(int jobId) {
        List<IPartModel> partsNeeded = new ArrayList<>();

        final String sql =
                "SELECT * "  +
                " FROM " + TABLE_NAME +
                " LEFT JOIN " + PARTS_NEEDED_TABLE_NAME + " ON " + TABLE_NAME + "." + ID_COLUMN +
                " = " + PARTS_NEEDED_TABLE_NAME + "." + PartNeeded_ID_COLUMN +
                " WHERE "  + JOB_ID_COLUMN + " =?";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, jobId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    IPartModel partModel = new PartModel(rs.getString(PART_NAME_COLUMN),
                            rs.getInt(QUANTITY_COLUMN));
                    partModel.setPartId(rs.getInt(ID_COLUMN));
                    partsNeeded.add(partModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve the part names from the database: " + e.getMessage());
        }

        return partsNeeded;
    }
}
