package com.sharepay.ug;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sharepay.ug.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SetupProfile extends AppCompatActivity {

    private TextView resultTextView;
    String phoneNumber;
    private EditText firstname, lastname;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        // Create a ProgressDialog to show while processing
        progressDialog = new ProgressDialog(SetupProfile.this);
        progressDialog.setMessage("Setting up your Profile...");
        progressDialog.setCancelable(false);

        resultTextView = findViewById(R.id.errormsg);
        firstname = findViewById(R.id.f_name);
        lastname = findViewById(R.id.l_name);

        // Retrieve the phone number from the Intent
        phoneNumber = getIntent().getStringExtra("phone_number");

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

        progressDialog.show();

        // Create a Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://34.151.68.80/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the API interface
        Api api = retrofit.create(Api.class);

        // Calling the createUser API method
        User newUser = new User();
        newUser.setPhone(phoneNumber);
        newUser.setFirstName(firstname.getText().toString());
        newUser.setLastName(lastname.getText().toString());
        newUser.setPhoto("photo_url");

        Call<ResponseBody> createUserCall = api.createUser(newUser);

        createUserCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string(); // Convert response to string
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String message = jsonObject.optString("message");

                        // Handle the response message
                        SaveUser();
                        resultTextView.setText("User creation: " + message);

                        progressDialog.dismiss();

                        // Load the Main Activity
                        Intent intent = new Intent(SetupProfile.this, MainActivity.class);
                        startActivity(intent);

                        finish();

                    } catch (IOException e) {
                        e.printStackTrace();
                        resultTextView.setText("Check your Internet Connection.");
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        resultTextView.setText("Check your Internet Connection.");
                        progressDialog.dismiss();
                    }
                } else {
                    // Handle the error
                    resultTextView.setText("Failed to create a new user.");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle network failure and log the error
                Log.e("NetworkError", "Network request failed: " + t.getMessage());
                resultTextView.setText("Network request failed.");
            }
        });
    }

    // Save User to Local Database
    private  void SaveUser(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        User user = new User();
        user.setPhone(phoneNumber);
        user.setFirstName(firstname.getText().toString());
        user.setLastName(lastname.getText().toString());
        long userId = dbHelper.insertUser(user);
    }



}