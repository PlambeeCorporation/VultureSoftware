package com.plambeeco.helper;

import java.util.Stack;

public class ViewHelper {
    public static final String JOB_VIEW_RESOURCE = "jobviews/jobview.fxml";
    public static final String TECHNICIANS_TASK_VIEW_RESOURCE = "tasksview/techniciantasksview.fxml";
    public static final String ASSIGN_TASKS_VIEW_RESOURCE = "tasksview/assigntaskview.fxml";
    public static final String UNASSIGNED_TASKS_VIEW_RESOURCE = "tasksview/unassignedtasksview.fxml";
    public static final String ALL_JOBS_VIEW_RESOURCE = "jobviews/alljobview.fxml";
    public static final String ROOT_TECHNICIAN_VIEW_RESOURCE = "roottechnicianview.fxml";
    public static final String OVERDUE_OR_UNFINISHED_TASKS_VIEW_RESOURCE = "tasksview/overdueorunfinishedtasksview.fxml";

    private static Stack<String> viewsResourcesStack = new Stack<>();

    public static Stack<String> getViewsResourcesStack() {
        return viewsResourcesStack;
    }
}
