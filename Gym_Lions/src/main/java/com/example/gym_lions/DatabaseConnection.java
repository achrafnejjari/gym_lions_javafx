package com.example.gym_lions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/gym";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");


            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection error: " + e.getMessage());
            return null;
        }
    }
}
