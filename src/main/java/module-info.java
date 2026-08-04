module org.example.fund_tracker_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires YahooFinanceAPI;
    requires jackson.annotations;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;


    opens org.example.fund_tracker_project to javafx.fxml;
    exports org.example.fund_tracker_project;
    opens org.example.fund_tracker_project.controller to javafx.fxml;
}