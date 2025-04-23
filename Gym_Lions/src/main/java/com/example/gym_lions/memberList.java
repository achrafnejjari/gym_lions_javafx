package com.example.gym_lions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class memberList {

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, Integer> member_id;
    @FXML
    private TableColumn<User, String> first_name;
    @FXML
    private TableColumn<User, String> last_name;
    @FXML
    private TableColumn<User, String> address;
    @FXML
    private TableColumn<User, String> start_date;
    @FXML
    private TableColumn<User, String> end_date;
    @FXML
    private TableColumn<User, Double> amount;
    @FXML
    private TableColumn<User, String> gender;
    @FXML
    Button btnAdd;
    @FXML
    Button btnRefresh;
    @FXML
    Button btnPayment;
    @FXML
    Button btnList;

    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        // Configurer les colonnes
        member_id.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        first_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        last_name.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        start_date.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        end_date.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Charger les données dans la table
        userList = FXCollections.observableArrayList();
        loadDataFromDatabase();
        tableView.setItems(userList);
    }

    private void loadDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/gym";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String address = resultSet.getString("address");
                String startDate = resultSet.getString("start_date");
                String endDate = resultSet.getString("end_date");
                double amount = resultSet.getDouble("amount");
                String gender = resultSet.getString("gender");

                // Ajouter un utilisateur à la liste
                userList.add(new User(id, firstName, lastName, address, startDate, endDate, amount, gender));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
