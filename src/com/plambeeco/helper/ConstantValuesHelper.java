package com.plambeeco.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.List;

public class ConstantValuesHelper {
    public static final String ADD_NEW_TASK_NAME = "Add new Task Name";
    public static final String ADD_NEW_PART_NAME = "Add new Part Name";
    public static final ObservableList<String> TASK_PRIORITIES = FXCollections.observableArrayList("Low", "Medium", "High", "Very High");
}
