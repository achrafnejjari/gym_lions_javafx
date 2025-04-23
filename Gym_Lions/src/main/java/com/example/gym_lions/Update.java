package com.example.gym_lions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Update {

    @FXML
    private TextField idSearchField; // TextField to enter ID for search
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField startDateField;
    @FXML
    private TextField endDateField;
    @FXML
    private TextField amountField;
    @FXML
    Button btnAdd;
    @FXML
    Button btnRefresh;
    @FXML
    Button btnPayment;
    @FXML
    Button btnList;
    @FXML
    private Button clearButton2;
    @FXML
    private Button saveButton;

    // Method to search for a user by ID and populate the text fields
    @FXML
    public void OnsearchButton(ActionEvent event) {
        String id = idSearchField.getText();

        // Reset red borders before search
        resetFieldBorders();

        if (id.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Please enter an ID to search.");
            idSearchField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            return;
        }

        try {
            // Fetch user data from the database based on the entered ID
            String query = "SELECT * FROM users WHERE id = ?";
            try (Connection connection = DatabaseConnection.connect();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Populate the text fields with the fetched data
                    firstNameField.setText(resultSet.getString("first_name"));
                    lastNameField.setText(resultSet.getString("last_name"));
                    addressField.setText(resultSet.getString("address"));
                    startDateField.setText(resultSet.getString("start_date"));
                    endDateField.setText(resultSet.getString("end_date"));
                    amountField.setText(resultSet.getString("amount"));
                } else {
                    showAlert(AlertType.ERROR, "Search Error", "No user found with that ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Error while fetching data from the database.");
        }
    }

    // Method for updating or inserting the user information in the database
    @FXML
    public void onUpdate(ActionEvent event) {
        String id = idSearchField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        String amount = amountField.getText();

        // Reset red borders before update
        resetFieldBorders();

        // Check if all fields are filled
        if (id.isEmpty()) {
            idSearchField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        }
        if (firstName.isEmpty()) {
            firstNameField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        }
        if (lastName.isEmpty()) {
            lastNameField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        }
        if (address.isEmpty()) {
            addressField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        }
        if (startDate.isEmpty()) {
            startDateField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        }
        if (endDate.isEmpty()) {
            endDateField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        }
        if (amount.isEmpty()) {
            amountField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        }

        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || amount.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error", "All fields are required!");
            return;
        }

        try (Connection connection = DatabaseConnection.connect()) {
            // Check if the user exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, id);
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();

                if (resultSet.getInt(1) > 0) {
                    // User exists, update the data
                    String updateQuery = "UPDATE users SET first_name = ?, last_name = ?, address = ?, start_date = ?, end_date = ?, amount = ? WHERE id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, firstName);
                        updateStatement.setString(2, lastName);
                        updateStatement.setString(3, address);
                        updateStatement.setString(4, startDate);
                        updateStatement.setString(5, endDate);
                        updateStatement.setString(6, amount);
                        updateStatement.setString(7, id);

                        updateStatement.executeUpdate();
                        showAlert(AlertType.INFORMATION, "Update Successful", "User information has been updated successfully!");
                    }
                } else {
                    // User does not exist, insert new data
                    String insertQuery = "INSERT INTO users (id, first_name, last_name, address, start_date, end_date, amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, id);
                        insertStatement.setString(2, firstName);
                        insertStatement.setString(3, lastName);
                        insertStatement.setString(4, address);
                        insertStatement.setString(5, startDate);
                        insertStatement.setString(6, endDate);
                        insertStatement.setString(7, amount);

                        insertStatement.executeUpdate();
                        showAlert(AlertType.INFORMATION, "Insert Successful", "New user has been added successfully!");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Error while saving data to the database.");
        }
    }

    // Method for clearing the fields
    @FXML
    public void onClearButton(ActionEvent event) {
        firstNameField.clear();
        lastNameField.clear();
        addressField.clear();
        startDateField.clear();
        endDateField.clear();
        amountField.clear();
        idSearchField.clear(); // Clear the search field as well

        // Reset borders when clearing
        resetFieldBorders();
    }

    // Helper method to reset red borders on fields
    private void resetFieldBorders() {
        firstNameField.setStyle(null);
        lastNameField.setStyle(null);
        addressField.setStyle(null);
        startDateField.setStyle(null);
        endDateField.setStyle(null);
        amountField.setStyle(null);
        idSearchField.setStyle(null); // Reset border for search field
    }

    // Helper method to display alerts
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void OnbtnLogout(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gym_lions/login.fxml"));

            // Create a new scene and set it on the primary stage
            Scene scene = new Scene(loader.load());
            Stage primaryStage = (Stage) btnAdd.getScene().getWindow(); // Get the current window (login window)
            primaryStage.setScene(scene); // Set the new scene
            primaryStage.show(); // Display the home scene

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void OnbtnAdd(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gym_lions/add.fxml"));

            // Create a new scene and set it on the primary stage
            Scene scene = new Scene(loader.load());
            Stage primaryStage = (Stage) btnAdd.getScene().getWindow(); // Get the current window (login window)
            primaryStage.setScene(scene); // Set the new scene
            primaryStage.show(); // Display the home scene

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void OnbtnRefresh(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gym_lions/update.fxml"));

            // Create a new scene and set it on the primary stage
            Scene scene = new Scene(loader.load());
            Stage primaryStage = (Stage) btnRefresh.getScene().getWindow(); // Get the current window (login window)
            primaryStage.setScene(scene); // Set the new scene
            primaryStage.show(); // Display the home scene

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void OnbtnPayment(ActionEvent actionEvent) throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gym_lions/payment.fxml"));

            // Create a new scene and set it on the primary stage
            Scene scene = new Scene(loader.load());
            Stage primaryStage = (Stage) btnPayment.getScene().getWindow(); // Get the current window (login window)
            primaryStage.setScene(scene); // Set the new scene
            primaryStage.show(); // Display the home scene

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void OnbtnList(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gym_lions/membersList.fxml"));

            // Create a new scene and set it on the primary stage
            Scene scene = new Scene(loader.load());
            Stage primaryStage = (Stage) btnList.getScene().getWindow(); // Get the current window (login window)
            primaryStage.setScene(scene); // Set the new scene
            primaryStage.show(); // Display the home scene

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
