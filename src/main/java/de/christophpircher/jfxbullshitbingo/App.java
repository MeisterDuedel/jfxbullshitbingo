package de.christophpircher.jfxbullshitbingo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Bullshit Bingo");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e->{
            stage.close();
            System.exit(0);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

}