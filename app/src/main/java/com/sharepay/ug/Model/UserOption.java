package com.sharepay.ug.Model;

public class UserOption {
    private String phone;
    private String first_name;
    private String last_name;

    public UserOption(String phone, String first_name, String last_name) {
        this.phone = phone;
        this.first_name = first_name;
        this.last_name = last_name;
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


    // You can also add setters if needed
}