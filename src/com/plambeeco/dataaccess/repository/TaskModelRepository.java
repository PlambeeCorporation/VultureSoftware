package com.plambeeco.dataaccess.repository;

import com.plambeeco.dataaccess.dataprocessor.PersonModelProcessor;
import com.plambeeco.helper.ConstantValuesHelper;
import com.plambeeco.models.ITaskModel;
import com.plambeeco.models.TaskModel;

import java.sql.*;
import java.util.*;

public class TaskModelRepository implements ITaskModelRepository {
    private static final String TABLE_NAME = "Tasks";
    private static final String ID_COLUMN = "Id";
    private static final String TASK_NAME_COLUMN = "TaskName";
    private static final String TASK_PRIORITY_COLUMN = "TaskPriority";
    private static final String TASK_NOTES_COLUMN = "TaskNotes";
    private static final String HOURS_NEEDED_COLUMN = "HoursNeeded";
    private static final String TASK_COMPLETED_COLUMN = "TaskCompleted";
    private static final String TASK_ID_FOREIGN_KEY = "TaskId";

    private static final String JOB_TABLE = "Job";
    private static final String JOB_ID_COLUMN = "Id";
    private static final String JOB_DETAILS_FOREIGN_ID_COLUMN = "JobDetailsId";

    private static final String JOB_TASKS_TABLE_NAME = "JobTasks";
    private static final String JOB_ID_FOREIGN_KEY_COLUMN = "JobId";

    private static final String JOB_DETAILS_TABLE = "JobDetails";
    private static final String JOB_DETAILS_ID_COLUMN = "Id";
    private static final String JOB_DETAILS_RETURN_DATE_COLUMN = "ReturnDate";

    private static final String TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME = "TechnicianAssignedTasks";
    private static final String TECHNICIAN_ID = "TechnicianId";

    @Override
    public void add(ITaskModel taskModel) {
        final String checkSQL = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + ID_COLUMN + " =?";

        final String sql =
                "INSERT INTO " + TABLE_NAME + "(" + TASK_NAME_COLUMN + "," + TASK_PRIORITY_COLUMN + ", " +
                        TASK_NOTES_COLUMN + ", " + HOURS_NEEDED_COLUMN +  ", " + TASK_COMPLETED_COLUMN + ") " +
                        "VALUES(?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement psCheck = con.prepareStatement(checkSQL);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            psCheck.setInt(1, taskModel.getTaskId());

            ResultSet resultAlreadyExists = psCheck.executeQuery();

            if(!resultAlreadyExists.next()) {
                ps.setString(1, taskModel.getTaskName());
                ps.setString(2, taskModel.getTaskPriority());
                ps.setString(3, taskModel.getTaskNotes());
                ps.setInt(4, taskModel.getHoursNeeded());
                ps.setBoolean(5, taskModel.isTaskCompleted());

                int affectedRows = ps.executeUpdate();

                if(affectedRows == 0){
                    throw new SQLException("Adding task failed, no rows affected");
                }

                try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        taskModel.setTaskId(generatedKeys.getInt(1));
                    }else{
                        throw new SQLException("Adding task failed, no ID obtained");
                    }
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void addTasksNeeded(int jobId, Collection<ITaskModel> tasks) {
        final String insertSQL =
                "INSERT INTO " + JOB_TASKS_TABLE_NAME + "(" + JOB_ID_FOREIGN_KEY_COLUMN + "," + TASK_ID_FOREIGN_KEY + ")" +
                        "VALUES(?,?)";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement psInsert = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)){
            psInsert.setInt(1, jobId);
            for (ITaskModel taskModel : tasks){
                psInsert.setInt(2, taskModel.getTaskId());
                int affectedRows = psInsert.executeUpdate();

                if(affectedRows == 0){
                    throw new SQLException("Adding task failed, no rows affected");
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void addTaskAssignedTechnician(ITaskModel task, int technicianId) {
        final String checkSQL = "SELECT * FROM " + TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME +
                " WHERE " + TASK_ID_FOREIGN_KEY + " =?" + " AND " +
                TECHNICIAN_ID + " =?";

        final String sql =
                "INSERT INTO " + TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME + " (" + TASK_ID_FOREIGN_KEY + "," + TECHNICIAN_ID + ") " +
                        "VALUES(?,?)";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
            PreparedStatement psCheck = con.prepareStatement(checkSQL);
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            psCheck.setInt(1, task.getTaskId());
            psCheck.setInt(2, technicianId);
            ResultSet checkResult = psCheck.executeQuery();

            if(!checkResult.next()){
                ps.setInt(1, task.getTaskId());
                ps.setInt(2, technicianId);

                int affectedRows = ps.executeUpdate();

                if(affectedRows == 0){
                    throw new SQLException("Adding task failed, no rows affected");
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void addAll(Collection<ITaskModel> tasks) {
        final String sql =
                "INSERT INTO " + TABLE_NAME + "(" + TASK_NAME_COLUMN + "," + TASK_PRIORITY_COLUMN + ", " +
                        TASK_NOTES_COLUMN + ", " + HOURS_NEEDED_COLUMN +  ", " + TASK_COMPLETED_COLUMN + ") " +
                        "VALUES(?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            for (ITaskModel taskModel : tasks){
                ps.setString(1, taskModel.getTaskName());
                ps.setString(2, taskModel.getTaskPriority());
                ps.setString(3, taskModel.getTaskNotes());
                ps.setInt(4, taskModel.getHoursNeeded());
                ps.setBoolean(5, taskModel.isTaskCompleted());

                int affectedRows = ps.executeUpdate();

                if(affectedRows == 0){
                    throw new SQLException("Adding task failed, no rows affected");
                }

                try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        taskModel.setTaskId(generatedKeys.getInt(1));
                    }else{
                        throw new SQLException("Adding task failed, no ID obtained");
                    }
                }
            }

        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void addTaskName(String taskName) {
        final String sql =
                "INSERT INTO " + TABLE_NAME + "(" + TASK_NAME_COLUMN + ")"  +
                        "VALUES(?)";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, taskName);
            int affectedRows = ps.executeUpdate();

            if(affectedRows == 0){
                throw new SQLException("Adding task name failed, no rows affected");
            }
        }catch(SQLException e){
            System.out.println("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void update(ITaskModel taskModel) {
        final String sql =
                "Update " + TABLE_NAME +
                        " SET( " + TASK_NAME_COLUMN + ", " + TASK_PRIORITY_COLUMN + ", " +
                        TASK_NOTES_COLUMN + ", " + HOURS_NEEDED_COLUMN + "," + TASK_COMPLETED_COLUMN +
                        ")=" + "(?,?,?,?,?)" +
                        " WHERE " +
                        ID_COLUMN + " =?";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
           PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, taskModel.getTaskName());
            ps.setString(2, taskModel.getTaskPriority());
            ps.setString(3, taskModel.getTaskNotes());
            ps.setInt(4, taskModel.getHoursNeeded());
            ps.setBoolean(5, taskModel.isTaskCompleted());
            ps.setInt(6, taskModel.getTaskId());
            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to update the Task: " + e.getMessage());
        }
    }

    @Override
    public void updateTaskName(String oldTaskName, String newTaskName) {
        final String sql =
                "Update " + TABLE_NAME +
                        " SET( " + TASK_NAME_COLUMN +
                        ")=" + "(?)" +
                        " WHERE " +
                        TASK_NAME_COLUMN + " =?;";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, newTaskName);
            ps.setString(2, oldTaskName);

            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to update the Task Name: " + e.getMessage());
        }
    }

    @Override
    public void remove(ITaskModel taskModel) {
        final String sql = "DELETE FROM " + TABLE_NAME + " WHERE Id=? ";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, taskModel.getTaskId());

            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to delete task from the database: " + e.getMessage());
        }
    }

    @Override
    public void removeJobTask(int jobId, int taskId) {
        final String sql = "DELETE FROM " + JOB_TASKS_TABLE_NAME +
                    " WHERE " + JOB_ID_FOREIGN_KEY_COLUMN + " =?" + " AND " +
                TASK_ID_FOREIGN_KEY + " =?";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, jobId);
            ps.setInt(2, taskId);

            ps.execute();

        }catch(SQLException e){
            System.out.println("Failed to delete task from the database: " + e.getMessage());
        }
    }

    @Override
    public void removeTaskAssignedTechnician(int taskId, int technicianId) {
        final String sql = "DELETE FROM " + TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME +
                " WHERE " + TASK_ID_FOREIGN_KEY + " =?" + " AND " +
                TECHNICIAN_ID + " =?";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, taskId);
            ps.setInt(2, technicianId);

            ps.execute();

        }catch(SQLException e) {
            System.out.println("Failed to delete task from the database: " + e.getMessage());
        }
    }

    @Override
    public ITaskModel getById(int id) {
        final String sql =
                        "SELECT *"  +
                        " FROM " + TABLE_NAME +
                        " WHERE " + ID_COLUMN + "= ?";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    ITaskModel taskModel = new TaskModel(rs.getString(TASK_NAME_COLUMN),
                                                         rs.getString(TASK_PRIORITY_COLUMN),
                                                         rs.getString(TASK_NOTES_COLUMN),
                                                         rs.getInt(HOURS_NEEDED_COLUMN),
                                                         null,
                                                         rs.getBoolean(TASK_COMPLETED_COLUMN));
                    taskModel.setTaskId(rs.getInt(ID_COLUMN));
                    return taskModel;
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve from the database: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ITaskModel> getAll() {
        List<ITaskModel> tasks = new ArrayList<>();
        final String sql =
                "SELECT * " +
                        "FROM " +
                        TABLE_NAME +
                        " WHERE " + TASK_COMPLETED_COLUMN + " != NULL";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    ITaskModel taskModel = new TaskModel(rs.getString(TASK_NAME_COLUMN),
                            rs.getString(TASK_PRIORITY_COLUMN),
                            rs.getString(TASK_NOTES_COLUMN),
                            rs.getInt(HOURS_NEEDED_COLUMN),
                            null,
                            rs.getBoolean(TASK_COMPLETED_COLUMN));
                    taskModel.setTaskId(rs.getInt(ID_COLUMN));
                    tasks.add(taskModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve from the database: " + e.getMessage());
        }

        return tasks;
    }

    @Override
    public List<ITaskModel> getAllCompletedTasks() {
        List<ITaskModel> tasks = new ArrayList<>();
        final String sql =
                "SELECT " + JOB_ID_FOREIGN_KEY_COLUMN + ", " + TABLE_NAME + ".* " +
                        "FROM " + JOB_TASKS_TABLE_NAME +
                        " LEFT JOIN " + TABLE_NAME + " on " + TASK_ID_FOREIGN_KEY + " = " +
                        TABLE_NAME + "." + ID_COLUMN +
                        " WHERE " + TASK_COMPLETED_COLUMN + " = True";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    ITaskModel taskModel = new TaskModel(rs.getString(TASK_NAME_COLUMN),
                            rs.getString(TASK_PRIORITY_COLUMN),
                            rs.getString(TASK_NOTES_COLUMN),
                            rs.getInt(HOURS_NEEDED_COLUMN),
                            PersonModelProcessor.getAssignedTechnicians(rs.getInt(ID_COLUMN)),
                            rs.getBoolean(TASK_COMPLETED_COLUMN));
                    taskModel.setTaskId(rs.getInt(ID_COLUMN));
                    tasks.add(taskModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve from the database: " + e.getMessage());
        }

        return tasks;
    }

    @Override
    public List<ITaskModel> getAllNotCompletedTasks() {
        List<ITaskModel> tasks = new ArrayList<>();
        final String sql =
                "SELECT " + JOB_ID_FOREIGN_KEY_COLUMN + ", " + TABLE_NAME + ".* " +
                        "FROM " + JOB_TASKS_TABLE_NAME +
                        " LEFT JOIN " + TABLE_NAME + " on " + TASK_ID_FOREIGN_KEY + " = " +
                        TABLE_NAME + "." + ID_COLUMN +
                        " WHERE " + TASK_COMPLETED_COLUMN + " = False";


        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    ITaskModel taskModel = new TaskModel(rs.getString(TASK_NAME_COLUMN),
                            rs.getString(TASK_PRIORITY_COLUMN),
                            rs.getString(TASK_NOTES_COLUMN),
                            rs.getInt(HOURS_NEEDED_COLUMN),
                            PersonModelProcessor.getAssignedTechnicians(rs.getInt(ID_COLUMN)),
                            rs.getBoolean(TASK_COMPLETED_COLUMN));
                    taskModel.setTaskId(rs.getInt(ID_COLUMN));
                    taskModel.setJobId(rs.getInt(JOB_ID_FOREIGN_KEY_COLUMN));
                    tasks.add(taskModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve from the database: " + e.getMessage());
        }

        return tasks;
    }

    @Override
    public List<ITaskModel> getAllUnassignedTasks() {
        List<ITaskModel> tasks = new ArrayList<>();
        final String sql =
                "SELECT " + JOB_ID_FOREIGN_KEY_COLUMN + ", " + TABLE_NAME + ".* " +
                        "FROM " + JOB_TASKS_TABLE_NAME +
                        " INNER JOIN " + TABLE_NAME + " on " + JOB_TASKS_TABLE_NAME + "." + TASK_ID_FOREIGN_KEY + " = " +
                        TABLE_NAME + "." + ID_COLUMN +
                        " LEFT JOIN " + TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME + " on " + TABLE_NAME + "." + ID_COLUMN +
                        " = " + TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME + "." + TASK_ID_FOREIGN_KEY +
                        " WHERE " + TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME + "."  + TECHNICIAN_ID + " IS NULL";


        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    ITaskModel taskModel = new TaskModel(rs.getString(TASK_NAME_COLUMN),
                            rs.getString(TASK_PRIORITY_COLUMN),
                            rs.getString(TASK_NOTES_COLUMN),
                            rs.getInt(HOURS_NEEDED_COLUMN),
                            rs.getBoolean(TASK_COMPLETED_COLUMN));
                    taskModel.setTaskId(rs.getInt(ID_COLUMN));
                    taskModel.setJobId(rs.getInt(JOB_ID_FOREIGN_KEY_COLUMN));
                    tasks.add(taskModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve from the database: " + e.getMessage());
        }

        return tasks;
    }

    @Override
    public List<ITaskModel> getAllOverdueTasks() {
        List<ITaskModel> tasks = new ArrayList<>();
        final String sql =
                "SELECT " + JOB_ID_FOREIGN_KEY_COLUMN + ", " + TABLE_NAME + ".* " +
                        "FROM " + JOB_TABLE +
                        " INNER JOIN " + JOB_DETAILS_TABLE + " on " + JOB_TABLE + "." + JOB_DETAILS_FOREIGN_ID_COLUMN + " = " +
                        JOB_DETAILS_TABLE + "." + JOB_DETAILS_ID_COLUMN +
                        " INNER JOIN " + JOB_TASKS_TABLE_NAME + " on " + JOB_TABLE + "." + JOB_ID_COLUMN + " = " +
                        JOB_TASKS_TABLE_NAME + "." + JOB_ID_FOREIGN_KEY_COLUMN +
                        " INNER JOIN " + TABLE_NAME + " on " + JOB_TASKS_TABLE_NAME + "." + TASK_ID_FOREIGN_KEY + " = " +
                        TABLE_NAME + "." + ID_COLUMN +
                        " WHERE " + JOB_DETAILS_TABLE + "."  + JOB_DETAILS_RETURN_DATE_COLUMN + " < CURRENT_DATE AND " +
                        TABLE_NAME + "." + TASK_COMPLETED_COLUMN + " IS FALSE";


        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    ITaskModel taskModel = new TaskModel(rs.getString(TASK_NAME_COLUMN),
                            rs.getString(TASK_PRIORITY_COLUMN),
                            rs.getString(TASK_NOTES_COLUMN),
                            rs.getInt(HOURS_NEEDED_COLUMN),
                            rs.getBoolean(TASK_COMPLETED_COLUMN));
                    taskModel.setTaskId(rs.getInt(ID_COLUMN));
                    taskModel.setJobId(rs.getInt(JOB_ID_FOREIGN_KEY_COLUMN));
                    tasks.add(taskModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve from the database: " + e.getMessage());
        }

        return tasks;
    }

    @Override
    public Set<String> getAllTaskNames() {
        Set<String> taskNames = new HashSet<>();

        final String sql =
                "SELECT DISTINCT " + TASK_NAME_COLUMN +
                        " FROM " + TABLE_NAME;

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    taskNames.add(rs.getString(TASK_NAME_COLUMN));
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve the task names from the database: " + e.getMessage());
        }

        return taskNames;
    }

    @Override
    public List<ITaskModel> getJobTasksNeeded(int jobId) {
        List<ITaskModel> tasksNeeded = new ArrayList<>();

        final String sql =
                "SELECT * "  +
                        " FROM " + TABLE_NAME +
                        " LEFT JOIN " + JOB_TASKS_TABLE_NAME + " ON " + TABLE_NAME + "." + ID_COLUMN +
                        " = " + JOB_TASKS_TABLE_NAME + "." + TASK_ID_FOREIGN_KEY +
                        " WHERE "  + JOB_ID_FOREIGN_KEY_COLUMN + " =?";

        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, jobId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    ITaskModel taskModel = new TaskModel(rs.getString(TASK_NAME_COLUMN),
                            rs.getString(TASK_PRIORITY_COLUMN),
                            rs.getString(TASK_NOTES_COLUMN),
                            rs.getInt(HOURS_NEEDED_COLUMN),
                            PersonModelProcessor.getAssignedTechnicians(rs.getInt(ID_COLUMN)),
                            rs.getBoolean(TASK_COMPLETED_COLUMN));
                    taskModel.setTaskId(rs.getInt(ID_COLUMN));
                    taskModel.setJobId(jobId);
                    tasksNeeded.add(taskModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve the tasks from the database: " + e.getMessage());
        }
        return tasksNeeded;

    }

    @Override
    public List<ITaskModel> getTechniciansCurrentlyAssignedTasks(int technicianId) {
        List<ITaskModel> tasks = new ArrayList<>();
        final String sql =
                "SELECT " + TABLE_NAME + ".*, " + JOB_TASKS_TABLE_NAME + "." + JOB_ID_FOREIGN_KEY_COLUMN +
                        " FROM " + TABLE_NAME +
                        " LEFT JOIN " + TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME + " on " +
                        TABLE_NAME + "." + ID_COLUMN + " = " +
                        TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME + "." + TASK_ID_FOREIGN_KEY +
                        " LEFT JOIN " + JOB_TASKS_TABLE_NAME + " on " + JOB_TASKS_TABLE_NAME + "." + TASK_ID_FOREIGN_KEY +
                        " = " +      TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME + "." + TASK_ID_FOREIGN_KEY +
                        " WHERE " + TECHNICIAN_TASKS_ASSIGNED_TABLE_NAME + "." + TECHNICIAN_ID +
                        "=? ";



        try (Connection con = DriverManager.getConnection(ConstantValuesHelper.CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)){
             ps.setInt(1, technicianId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    ITaskModel taskModel = new TaskModel(rs.getString(TASK_NAME_COLUMN),
                            rs.getString(TASK_PRIORITY_COLUMN),
                            rs.getString(TASK_NOTES_COLUMN),
                            rs.getInt(HOURS_NEEDED_COLUMN),
                            rs.getBoolean(TASK_COMPLETED_COLUMN));
                    taskModel.setTaskId(rs.getInt(ID_COLUMN));
                    taskModel.setJobId(rs.getInt(JOB_ID_FOREIGN_KEY_COLUMN));
                    tasks.add(taskModel);
                }
            }
        }catch(SQLException e){
            System.out.println("Failed to retrieve from the database: " + e.getMessage());
        }

        return tasks;
    }
}
