module Vulture {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens com.plambeeco;
    opens com.plambeeco.view;
    opens com.plambeeco.view.recordjobviews;
    opens com.plambeeco.view.assigningtasksview;
    opens com.plambeeco.view.inspectjobviews;
    opens com.plambeeco.view.loginviews;
    opens com.plambeeco.view.jobviews;
}