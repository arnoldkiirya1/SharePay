package com.sharepay.ug;

import com.sharepay.ug.Model.Group;
import com.sharepay.ug.Model.GroupOption;
import com.sharepay.ug.Model.GroupMember;
import com.sharepay.ug.Model.Messages;
import com.sharepay.ug.Model.User;
import com.sharepay.ug.Model.UserOption;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface  Api {

    // Defined API methods here
    @POST("createUser.php")
    Call<ResponseBody> createUser(@Body User user);

    @GET("readUser.php")
    Call<User> readUser(@Query("user_id") int userId);

    @GET("getUsers.php")
    Call<List<User>> getUsers();

    // Add an API method for creating a message
    @POST("createMessage.php")
    Call<String> createMessage(@Body Messages messages);

    @GET("getMessages.php")
    Call<List<Messages>> getMessages(@Query("group_id") String groupId);

    @POST("createGroup.php")
    Call<ResponseBody> createGroup(@Body Group group);

    @POST("searchUsers.php")
    Call<List<UserOption>> searchUsers(@Query("phone") String phone);

    @POST("addGroupMember.php")
    Call<ResponseBody> addGroupMember(@Body GroupMember groupMember);

    // New endpoint for fetching group members
    @POST("getGroupMembers.php")
    Call<List<GroupOption>> getGroupMembers(@Query("group_id") String group_id);


}
