module de.christophpircher.jfxbullshitbingo {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires com.google.gson;


    opens de.christophpircher.jfxbullshitbingo to javafx.fxml, retrofit2,com.google.gson;
    exports de.christophpircher.jfxbullshitbingo;
}