package com.example.gym_lions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Add {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField startDateField;
    @FXML
    private TextField Price;
    @FXML
    private ToggleGroup periodGroup;
    @FXML
    private ToggleGroup genderGroup;
    @FXML
    private RadioButton oneMonthRadio, threeMonthRadio, sixMonthRadio, oneYearRadio;
    @FXML
    private RadioButton maleRadioButton111, femaleRadioButton111;
    @FXML
    Button btnAdd;
    @FXML
    Button btnRefresh;
    @FXML
    Button btnPayment;
    @FXML
    Button btnList;

    public void initialize() {
        periodGroup = new ToggleGroup();
        oneMonthRadio.setToggleGroup(periodGroup);
        threeMonthRadio.setToggleGroup(periodGroup);
        sixMonthRadio.setToggleGroup(periodGroup);
        oneYearRadio.setToggleGroup(periodGroup);

        genderGroup = new ToggleGroup();
        maleRadioButton111.setToggleGroup(genderGroup);
        femaleRadioButton111.setToggleGroup(genderGroup);

        startDateField.setEditable(false);
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        startDateField.setText(formattedDate);
        Price.setEditable(false);

        periodGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            updatePriceField(newToggle);
        });
    }

    private void updatePriceField(Toggle selectedToggle) {
        if (selectedToggle != null) {
            RadioButton selectedRadioButton = (RadioButton) selectedToggle;
            String selectedPeriod = selectedRadioButton.getText();
            switch (selectedPeriod) {
                case "1 month" -> Price.setText("250");
                case "3 months" -> Price.setText("650");
                case "6 months" -> Price.setText("1200");
                case "1 Year" -> Price.setText("2500");
                default -> Price.setText("");
            }
        }
    }

    public void onSaveButtonClick(ActionEvent actionEvent) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String startDate = startDateField.getText();
        String gender = null;

        Toggle selectedGenderToggle = genderGroup.getSelectedToggle();
        if (selectedGenderToggle != null) {
            RadioButton selectedGenderRadioButton = (RadioButton) selectedGenderToggle;
            gender = selectedGenderRadioButton.getText();
        }

        Toggle selectedToggle = periodGroup.getSelectedToggle();
        String endDate = null;
        if (selectedToggle != null) {
            RadioButton selectedRadioButton = (RadioButton) selectedToggle;
            String selectedPeriod = selectedRadioButton.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
            switch (selectedPeriod) {
                case "1 month" -> endDate = startLocalDate.plusMonths(1).format(formatter);
                case "3 months" -> endDate = startLocalDate.plusMonths(3).format(formatter);
                case "6 months" -> endDate = startLocalDate.plusMonths(6).format(formatter);
                case "1 Year" -> endDate = startLocalDate.plusYears(1).format(formatter);
            }
        }

        String amount = Price.getText();
        if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || gender == null || endDate == null || amount.isEmpty()) {
            handleEmptyFields(firstName, lastName, address, amount);
            return;
        }

        try {
            insertDataIntoDatabase(firstName, lastName, address, startDate, endDate, amount, gender);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion des données : " + e.getMessage());
            showAlert("Error", "An error occurred while saving the data.");
        }
    }

    private void insertDataIntoDatabase(String firstName, String lastName, String address, String startDate, String endDate, String amount, String gender) throws SQLException {
        String sql = "INSERT INTO users (first_name, last_name, address, start_date, end_date, amount, gender) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, address);
            statement.setString(4, startDate);
            statement.setString(5, endDate);
            statement.setString(6, amount);
            statement.setString(7, gender);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("CONFIRMATION", "Data saved successfully!");
                onClearButton();
            } else {
                showAlert("ERROR", "Failed to save data.");
            }
        }
    }

    private void handleEmptyFields(String firstName, String lastName, String address, String amount) {
        if (firstName.isEmpty()) {
            firstNameField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            firstNameField.requestFocus();
        }
        if (lastName.isEmpty()) {
            lastNameField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            if (firstName.isEmpty()) lastNameField.requestFocus();
        }
        if (address.isEmpty()) {
            addressField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            if (firstName.isEmpty()) addressField.requestFocus();
        }
        if (amount.isEmpty()) {
            Price.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            if (firstName.isEmpty()) Price.requestFocus();
        }

        showAlert("Validation Error", "Please fill in all required fields.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @FXML
    private void onClearButton() {
        firstNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");
        Price.setText("");
        genderGroup.selectToggle(null);
        periodGroup.selectToggle(null);

        firstNameField.setStyle("");
        lastNameField.setStyle("");
        addressField.setStyle("");
        Price.setStyle("");
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



    // Helper method to load scenes

