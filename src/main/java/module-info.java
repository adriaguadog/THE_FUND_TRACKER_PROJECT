module org.example.fund_tracker_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;


    opens org.example.fund_tracker_project to javafx.fxml;
    exports org.example.fund_tracker_project;
    opens org.example.fund_tracker_project.controller to javafx.fxml;
}