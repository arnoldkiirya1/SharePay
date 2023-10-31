package com.sharepay.ug.Model;

public class GroupOption {
    private String user_name;
    private String user_id;
    private String collection_share;

    public GroupOption(String user_name, String user_id, String collection_share) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.collection_share = collection_share;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getCollectionShare() {
        return collection_share;
    }

    public void setCollectionShare(String collection_share) {
        this.collection_share = collection_share;
    }
}