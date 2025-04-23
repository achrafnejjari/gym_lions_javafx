package com.example.gym_lions;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Link the external CSS file
        scene.getStylesheets().add(getClass().getResource("/com/style/style.css").toExternalForm());

        // Ajouter une icône
        stage.getIcons().add(new Image(getClass().getResource("/images/gym2-removebg-preview.png").toString()));

        stage.setTitle("Gym Lion's");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}
