package com.example.gym_lions;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Home {

    @FXML
    private AnchorPane sidePane, incomePane, memberPane;

    @FXML
    private Label income;

    @FXML
    private TableView<Member> tableView;

    @FXML
    private TableColumn<Member, String> colName;

    @FXML
    private TableColumn<Member, String> colStartDate;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    // Database connection method
    private Connection connect() {
        String url = "jdbc:mysql://localhost:3306/gym"; // Replace with your DB URL
        String user = "root"; // Replace with your DB username
        String password = ""; // Replace with your DB password
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Initialize method to load data and populate UI
    @FXML
    public void initialize() {
        calculateMonthlyIncome(); // Initialize the monthly income
        loadNewMembers(); // Load new members
        populateChart(); // Populate the chart with data
    }

    // Method to calculate and update monthly income
    private void calculateMonthlyIncome() {
        String query = "SELECT SUM(amount) AS monthly_income \n" +
                "FROM users \n" +
                "WHERE MONTH(payment_date) = MONTH(CURRENT_DATE)\n";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                double totalIncome = rs.getDouble("monthly_income");
                // Update the income label with the calculated monthly income
                income.setText(String.format("$%.2f", totalIncome));
            } else {
                income.setText("$0.00"); // Default value if no payments
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load new members who joined in the current month
    private void loadNewMembers() {
        String query = "SELECT first_name, last_name, start_date FROM users WHERE MONTH(start_date) = MONTH(CURRENT_DATE)";
        List<Member> members = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                members.add(new Member(name, rs.getString("start_date")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Bind data to the table view
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tableView.getItems().setAll(members);

        // After loading new members, recalculate monthly income
        calculateMonthlyIncome();  // Ensure the income label is updated
    }

    // Method to populate the income chart
    private void populateChart() {
        String query = "SELECT MONTHNAME(payment_date) AS month, SUM(amount) AS total_income FROM payments GROUP BY MONTH(payment_date)";
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Income");

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String month = rs.getString("month");
                double totalIncome = rs.getDouble("total_income");
                series.getData().add(new XYChart.Data<>(month, totalIncome));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        lineChart.getData().add(series); // Add data to the chart
    }

    // Inner class to represent Member data
    public static class Member {
        private String name;
        private String startDate;

        public Member(String name, String startDate) {
            this.name = name;
            this.startDate = startDate;
        }

        public String getName() {
            return name;
        }

        public String getStartDate() {
            return startDate;
        }
    }
}
