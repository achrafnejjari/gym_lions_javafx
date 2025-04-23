package com.example.gym_lions;

public class User {
    private int memberId;
    private String firstName;
    private String lastName;
    private String address;
    private String startDate;
    private String endDate;
    private double amount;
    private String gender;

    // Constructeur
    public User(int memberId, String firstName, String lastName, String address, String startDate, String endDate, double amount, String gender) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.gender = gender;
    }

    // Getters
    public int getMemberId() {
        return memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public double getAmount() {
        return amount;
    }

    public String getGender() {
        return gender;
    }
}
