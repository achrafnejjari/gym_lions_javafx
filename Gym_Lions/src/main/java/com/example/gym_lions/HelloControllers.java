package com.example.gym_lions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloControllers {
    @FXML
    TextField id_field;
    @FXML
    PasswordField password_field;
    @FXML
    Button login_btn;

    @FXML
    public void loginOnAction() {
        String name = id_field.getText();
        String password = password_field.getText();

        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT * FROM admins WHERE Name = ? AND Password = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        loadHomeScene();
                        System.out.println("Login successful!");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login Failed");
                        alert.setHeaderText(null); // Pas de texte d'en-tête
                        alert.setContentText("Invalid Name or Password");

                        alert.showAndWait(); // Affiche l'alerte

                        System.out.println("Invalid Name or Password.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadHomeScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gym_lions/add.fxml"));

            // Create a new scene and set it on the primary stage
            Scene scene = new Scene(loader.load());
            Stage primaryStage = (Stage) login_btn.getScene().getWindow(); // Get the current window (login window)
            primaryStage.setScene(scene); // Set the new scene
            primaryStage.show(); // Display the home scene

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
