package com.sharepay.ug.Model;

public class GroupMember {

    private String groupId; // The ID of the group to which the member belongs
    private String userName; // The name of the group member
    private String userId; // The user ID of the member
    private String collectionShare; // The collection share associated with the member

    public GroupMember() {
        // Default constructor
    }

    public GroupMember(String groupId, String userName, String userId, String collectionShare) {

        this.groupId = groupId;
        this.userName = userName;
        this.userId = userId;
        this.collectionShare = collectionShare;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCollectionShare() {
        return collectionShare;
    }

    public void setCollectionShare(String collectionShare) {
        this.collectionShare = collectionShare;
    }
}