module com.quangminh.hotelmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens com.quangminh.hotelmanagement to javafx.fxml;
    exports com.quangminh.hotelmanagement;
}