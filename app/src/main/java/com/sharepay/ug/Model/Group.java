package com.sharepay.ug.Model;

public class Group {
    private String group_name;
    private String group_icon;
    private String admin_id;
    private String desc;
    private String bill;
    private String target_fee;
    private String status;
    private String recent_transaction;

    public Group() {
        // Default constructor
    }

    public Group(String group_name, String group_icon, String admin_id, String desc, String bill, String target_fee, String status, String recent_transaction) {

        this.group_name = group_name;
        this.group_icon = group_icon;
        this.admin_id = admin_id;
        this.desc = desc;
        this.bill = bill;
        this.target_fee = target_fee;
        this.status = status;
        this.recent_transaction = recent_transaction;
    }


    public String getGroupName() {
        return group_name;
    }

    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }

    public String getGroupIcon() {
        return group_icon;
    }

    public void setGroupIcon(String group_icon) {
        this.group_icon = group_icon;
    }

    public String getAdminId() {
        return admin_id;
    }

    public void setAdminId(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getTargetFee() {
        return target_fee;
    }

    public void setTargetFee(String target_fee) {
        this.target_fee = target_fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecentTransaction() {
        return recent_transaction;
    }

    public void setRecentTransaction(String recent_transaction) {
        this.recent_transaction = recent_transaction;
    }
}
