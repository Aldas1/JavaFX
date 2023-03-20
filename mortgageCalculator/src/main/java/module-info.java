module com.skaiciuokle.bustoskaiciuokle {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;


    opens com.skaiciuokle.bustoskaiciuokle to javafx.fxml;
    exports com.skaiciuokle.bustoskaiciuokle;
    exports com.skaiciuokle.data;
    opens com.skaiciuokle.data to javafx.fxml;
}