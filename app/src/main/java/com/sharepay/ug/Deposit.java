package com.sharepay.ug;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.sharepay.ug.Collection.getRequestToPayStatus;
import com.sharepay.ug.Collection.getUserInformation;
import com.sharepay.ug.Collection.requestToPay;
import com.sharepay.ug.Sandbox_User.createUser;
import com.sharepay.ug.Sandbox_User.getToken;
import com.sharepay.ug.Sandbox_User.getUserApiKey;

import org.json.JSONException;

import java.io.IOException;

public class Deposit extends AppCompatActivity {

    // Static variables for configuration and data
    private static String PRIMARY_KEY = "4f3293fc5f404e7097e90e0b4a022f32";
    private static String CONTENT_TYPE = "application/json";
    private static String UUID;
    private static TextView statusMsg;
    private int CREATION_RESPONSE_CODE;
    private String TARGET_ENVIRONMENT;
    private static com.sharepay.ug.Sandbox_User.getUserApiKey getUserApiKey = new getUserApiKey();
    private EditText PHONE_NUMBER, AMOUNT;
    private String API_KEY, USER_TOKEN, AUTH_KEY;
    public String tmp;
    ProgressDialog progressDialog;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        // Initialize a ProgressDialog for showing loading messages
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        // Initialize variables
        API_KEY = "";

        // Get a reference to the "getTokenBtn" button
        final Button getTokenBtn = findViewById(R.id.getTokenBtn);

        // Set a click listener for the "getTokenBtn" button
        getTokenBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Define an AsyncTask for background processing
                final AsyncTask execute = new AsyncTask() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        // Generate a UUID and get phone number and amount from UI
                        UUID = java.util.UUID.randomUUID().toString();
                        PHONE_NUMBER = findViewById(R.id.phone_number);
                        AMOUNT = findViewById(R.id.amount);

                        try {
                            // Create a user and handle potential response codes
                            CREATION_RESPONSE_CODE = createUser.userCreation(PRIMARY_KEY, CONTENT_TYPE, UUID);
                            if (CREATION_RESPONSE_CODE == 400) {
                                return "INVALID_DATA";
                            } else if (CREATION_RESPONSE_CODE == 500) {
                                return "INTERNAL_SERVER_ERROR";
                            } else if (CREATION_RESPONSE_CODE == 201 || CREATION_RESPONSE_CODE == 409) {
                                // Obtain user information, API key, and authentication key
                                TARGET_ENVIRONMENT = getUserInformation.userInformation(PRIMARY_KEY, CONTENT_TYPE, UUID);
                                API_KEY = getUserApiKey.userApiKey(PRIMARY_KEY, CONTENT_TYPE, UUID);
                                AUTH_KEY = Base64.encodeToString((UUID.concat(":").concat(API_KEY)).getBytes(), Base64.NO_WRAP);
                                USER_TOKEN = getToken.theToken(PRIMARY_KEY, AUTH_KEY);
                                if (!USER_TOKEN.equals("INTERNAL_SERVER_ERROR") && !USER_TOKEN.equals("ERROR") && !USER_TOKEN.equals("UnAuthorized")) {
                                    // Perform payment request and get status
                                    requestToPay.getRequestToPay(PRIMARY_KEY, CONTENT_TYPE, UUID, USER_TOKEN, TARGET_ENVIRONMENT, AMOUNT.getText().toString(), PHONE_NUMBER.getText().toString());
                                    tmp = getRequestToPayStatus.getStatus(PRIMARY_KEY, CONTENT_TYPE, TARGET_ENVIRONMENT, UUID, USER_TOKEN);

                                    // Continuously check and display status
                                    while (true) {
                                        Handler handler = new Handler(Looper.getMainLooper());
                                        handler.post(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), tmp, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        SystemClock.sleep(60000); // Wait for 60 seconds

                                        // Repeatedly check status and perform other actions
                                        requestToPay.getRequestToPay(PRIMARY_KEY, CONTENT_TYPE, UUID, USER_TOKEN, TARGET_ENVIRONMENT, AMOUNT.getText().toString(), PHONE_NUMBER.getText().toString());
                                        tmp = getRequestToPayStatus.getStatus(PRIMARY_KEY, CONTENT_TYPE, TARGET_ENVIRONMENT, UUID, USER_TOKEN);

                                        System.out.println("tmp is:"+ tmp);
                                        getTokenAction(); // Perform another action
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
                execute.execute(); // Execute the AsyncTask
            }
        });
    }

    // Method to navigate to the Success activity
    public void getTokenAction() {
        Intent i = new Intent(this, Success.class);
        startActivity(i);
    }
}
