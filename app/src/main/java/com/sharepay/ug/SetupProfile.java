package com.sharepay.ug;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sharepay.ug.Model.User;

public class SetupProfile extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        resultTextView = findViewById(R.id.errormsg);

        // Find the button by its ID
        Button createbtn = findViewById(R.id.button);

        // Set an OnClickListener for the button
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create User
                createprofile();
            }
        });

    }

    public void createprofile(){

        // Create a Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://your-server-url.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the API interface
        Api api = retrofit.create(Api.class);

        // Calling the createUser API method
        User newUser = new User();
        newUser.setPhone("1234567890");
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setPhoto("photo_url");

        Call<String> createUserCall = api.createUser(newUser);

        createUserCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String message = response.body();
                    // Handle the response message
                    resultTextView.setText("User creation: " + message);
                } else {
                    // Handle the error
                    resultTextView.setText("Failed to create a new user.");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle network failure
                resultTextView.setText("Network request failed.");
            }
        });
    }

}