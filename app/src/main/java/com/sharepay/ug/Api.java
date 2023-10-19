package com.sharepay.ug;

import com.sharepay.ug.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface  Api {

    // Defined API methods here
    @POST("createUser.php")
    Call<String> createUser(@Body User user);

    @GET("readUser.php")
    Call<User> readUser(@Query("user_id") int userId);

    // Define other API methods as needed
}
