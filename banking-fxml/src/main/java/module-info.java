module com.quangminh.bankingfxml {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;


    opens com.quangminh.bankingfxml to javafx.fxml;
    exports com.quangminh.bankingfxml;

    opens com.quangminh.bankingfxml.control to javafx.fxml;
    exports  com.quangminh.bankingfxml.control;

    opens com.quangminh.bankingfxml.model to javafx.fxml;
    exports com.quangminh.bankingfxml.model;



}