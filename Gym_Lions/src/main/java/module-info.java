module com.example.gym_lions {
    requires javafx.fxml;
    requires java.sql;
    requires fontawesomefx;
    requires com.jfoenix;
    requires javafx.controls;


    opens com.example.gym_lions to javafx.fxml;
    exports com.example.gym_lions;
}