module org.example.routeapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.routeapp to javafx.fxml;
    exports org.example.routeapp;
}