package com.sharepay.ug.Model;

public class User {
    private int user_id;
    private String phone;
    private String first_name;
    private String last_name;
    private String photo;

    public User() {
        // Default constructor
    }

    public User(int user_id, String phone, String first_name, String last_name, String photo) {
        this.user_id = user_id;
        this.phone = phone;
        this.first_name = first_name;
        this.last_name = last_name;
        this.photo = photo;
    }

    // Getters and Setters
    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
