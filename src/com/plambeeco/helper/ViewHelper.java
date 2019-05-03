package com.plambeeco.helper;

import java.util.Stack;

public class ViewHelper {
    public static final String JOB_VIEW_RESOURCE = "jobviews/jobview.fxml";
    public static final String TECHNICIANS_TASK_VIEW_RESOURCE = "tasksview/techniciantasksview.fxml";
    public static final String ASSIGN_TASKS_VIEW_RESOURCE = "tasksview/assigntaskview.fxml";
    public static final String UNASSIGNED_TASKS_VIEW_RESOURCE = "tasksview/unassignedtasksview.fxml";
    public static final String ALL_JOBS_VIEW_RESOURCE = "jobviews/alljobview.fxml";
    public static final String ROOT_TECHNICIAN_VIEW_RESOURCE = "roottechnicianview.fxml";
    public static final String ROOT_HUMAN_RESOURCES_VIEW_RESOURCE = "roothumanresourcesview.fxml";
    public static final String UNFINISHED_TASKS_VIEW_RESOURCE = "tasksview/unfinishedtasksview.fxml";
    public static final String OVERDUE_TASKS_VIEW_RESOURCE = "tasksview/overduetasksview.fxml";
    public static final String CREATE_NEW_ACCOUNT_VIEW_RESOURCE = "accountviews/createnewaccountview.fxml";
    public static final String EDIT_OWN_ACCOUNT_VIEW_RESOURCE = "accountviews/editownaccountview.fxml";

    private static Stack<String> viewsResourcesStack = new Stack<>();

    public static Stack<String> getViewsResourcesStack() {
        return viewsResourcesStack;
    }
}
