module Vulture {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;
    requires jdk.rmic;

    opens com.plambeeco;
    opens com.plambeeco.view;
    opens com.plambeeco.view.recordjobviews;
    opens com.plambeeco.view.tasksview;
    opens com.plambeeco.view.loginviews;
    opens com.plambeeco.view.jobviews;
    opens com.plambeeco.view.jobviews.partsview;

}