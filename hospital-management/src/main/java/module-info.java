module com.quangminh.hospitalmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires java.desktop;

    opens com.quangminh.hospitalmanagement to javafx.fxml;
    exports com.quangminh.hospitalmanagement;

    opens com.quangminh.hospitalmanagement.model to javafx.fxml,javafx.base;
    exports com.quangminh.hospitalmanagement.model;

    opens com.quangminh.hospitalmanagement.control to javafx.fxml;
    exports com.quangminh.hospitalmanagement.control;

    opens com.quangminh.hospitalmanagement.util to javafx.fxml;
    exports com.quangminh.hospitalmanagement.util;
}