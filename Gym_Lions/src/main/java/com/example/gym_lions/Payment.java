package com.example.gym_lions;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class Payment {

    @FXML
    private Button btnAdd;
    @FXML
    private TableView<UserPayment> table;  // Use UserPayment type here instead of User
    @FXML
    private TableColumn<UserPayment, String> FullName;  // Correct the TableColumn type
    @FXML
    private TableColumn<UserPayment, String> EndDate;  // Correct the TableColumn type
    @FXML
    private TableColumn<UserPayment, Double> MoneyAmount;  // Correct the TableColumn type
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnPayment;
    @FXML
    private Button btnList;
    @FXML
    private Label new_clients;
    @FXML
    private Label our_total_amount;
    @FXML
    private Label total_users;
    @FXML
    private Label total_expired;

    private ObservableList<UserPayment> userList;

    // Logout button functionality (you can implement the logic here later)
    public void OnbtnLogout(ActionEvent actionEvent) {
        navigateToScene("/com/example/gym_lions/login.fxml");
    }

    // Navigate to the "Add" scene
    public void OnbtnAdd(ActionEvent actionEvent) {
        navigateToScene("/com/example/gym_lions/add.fxml");
    }

    // Navigate to the "Update" scene
    public void OnbtnRefresh(ActionEvent actionEvent) {
        navigateToScene("/com/example/gym_lions/update.fxml");
    }

    // Navigate to the "Payment" scene
    public void OnbtnPayment(ActionEvent actionEvent) throws IOException {
        navigateToScene("/com/example/gym_lions/payment.fxml");
    }

    // Navigate to the "Members List" scene
    public void OnbtnList(ActionEvent actionEvent) {
        navigateToScene("/com/example/gym_lions/membersList.fxml");
    }

    // Helper method to load a scene
    private void navigateToScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage primaryStage = (Stage) btnAdd.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Method to initialize data when the scene is loaded
    public void initialize() {
        if (new_clients != null && our_total_amount != null && total_users != null) {
            // Set the current number of people
            int newclients = getnewusers();
            new_clients.setText("+" + String.valueOf(newclients));

            // Set the total amount
            double totalAmount = getTotalAmount();
            our_total_amount.setText(String.valueOf(totalAmount) + " DH");

            // Set the total number of admins
            int totalusers = getAdminCount();
            total_users.setText(String.valueOf(totalusers));

            int totalexpired = gettotalexpired();
            total_expired.setText(String.valueOf(totalexpired));
        }

        // Configurer les colonnes (lie les tablecolum par les variable de class UserPayment ) pour saisir les donne
        FullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));  // "fullName" fait référence au nom de la propriété dans la classe modèle Java (par exemple, UserPayment).
        EndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));  // Corrected column mapping
        MoneyAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));  // Corrected column mapping

        // Charger les données dans la table
        userList = FXCollections.observableArrayList();
        loadDataFromDatabase();
        table.setItems(userList);
    }

    // Database query methods

    public int gettotalexpired() {
        String query = "SELECT COUNT(*) FROM users WHERE end_date < CURRENT_DATE";
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error in gettotalexpired: " + e.getMessage());
        }
        return 0;
    }

    public int getnewusers() {
        String query = "SELECT COUNT(*) FROM users WHERE MONTH(start_date) = MONTH(CURRENT_DATE)";
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error in getnewusers: " + e.getMessage());
        }
        return 0;
    }

    public double getTotalAmount() {
        String query = "SELECT SUM(amount) FROM users";
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error in getTotalAmount: " + e.getMessage());
        }
        return 0;
    }

    public int getAdminCount() {
        String query = "SELECT COUNT(*) FROM users";
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error in getAdminCount: " + e.getMessage());
        }
        return 0;
    }

    private void loadDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/gym";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            // Correction de la requête SQL
            String query = "SELECT CONCAT(first_name, ' - ', last_name) AS full_name, end_date, amount FROM users ORDER BY end_date ASC";
            ResultSet resultSet = statement.executeQuery(query);

            // Vérifier que la liste est initialisée
            if (userList == null) {
                userList = FXCollections.observableArrayList();
            }

            while (resultSet.next()) {
                String full_name = resultSet.getString("full_name"); // Correspond à l'alias SQL
                String endDate = resultSet.getString("end_date");
                double amount = resultSet.getDouble("amount");

                // Ajouter un utilisateur à la liste
                userList.add(new UserPayment(full_name, endDate, amount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
