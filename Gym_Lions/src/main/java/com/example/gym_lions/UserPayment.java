package com.example.gym_lions;

public class UserPayment {
    private String full_name;
    private String endDate;
    private double amount;

    // Constructeur
    public UserPayment(String full_name, String endDate, double amount) {
        this.full_name = full_name;
        this.endDate = endDate;
        this.amount = amount;
    }

    // Getters
    public String getFullName() {  // Change "getFirstName" to "getFullName"
        return full_name;
    }

    public String getEndDate() {
        return endDate;
    }

    public double getAmount() {
        return amount;
    }
}
