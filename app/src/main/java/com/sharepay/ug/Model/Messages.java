package com.sharepay.ug.Model;

public class Messages {

    private String user_id;
    private String name;
    private String group_id;
    private double amount_deposited;
    private double balance;
    private String timestamp;

    public Messages() {
        // Default constructor
    }

    public Messages(String user_id, String name, String group_id, double amount_deposited, double balance, String timestamp) {

        this.user_id = user_id;
        this.name = name;
        this.group_id = group_id;
        this.amount_deposited = amount_deposited;
        this.balance = balance;
        this.timestamp = timestamp;
    }

    // Getters and Setters for the fields

    public String getUserId() {
        return user_id;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return name;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getGroupId() {
        return group_id;
    }

    public void setGroupId(String group_id) {
        this.group_id = group_id;
    }

    public double getAmountDeposited() {
        return amount_deposited;
    }

    public void setAmountDeposited(double amountDeposited) {
        this.amount_deposited = amountDeposited;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}



