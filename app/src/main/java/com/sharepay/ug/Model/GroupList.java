package com.sharepay.ug.Model;

public class GroupList {
    private String group_name;
    private String recent_transaction;
    private String date_updated;
    private String admin_id;
    private String target_fee;
    private String bill;
    private String collection_share;
    private String group_id;
    private String status;

    public GroupList(String group_name, String recent_transaction, String date_updated, String admin_id, String target_fee, String bill, String collection_share, String group_id, String status) {
        this.group_name = group_name;
        this.recent_transaction = recent_transaction;
        this.date_updated = date_updated;
        this.admin_id = admin_id;
        this.target_fee = target_fee;
        this.bill = bill;
        this.collection_share = collection_share;
        this.group_id = group_id;
        this.status = status;
    }

    public String getGroupId() {
        return group_id;
    }

    public void setGroupId(String group_id) {
        this.group_id = group_id;
    }

    public String getGroupName() {
        return group_name;
    }

    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }

    public String getRecentTransaction() {
        return recent_transaction;
    }

    public void setRecentTransaction(String recent_transaction) {
        this.recent_transaction = recent_transaction;
    }

    public String getdateUpdated() {
        return date_updated;
    }

    public void setDateUpdated(String date_updated) {
        this.date_updated = date_updated;
    }

    public String getAdminId() {
        return admin_id;
    }

    public void setAdminId(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getTargetFee() {
        return target_fee;
    }

    public void setTargetFee(String target_fee) {
        this.target_fee = target_fee;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getCollectionShare() {
        return collection_share;
    }

    public void setCollectionShare(String collection_share) {
        this.collection_share = collection_share;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
