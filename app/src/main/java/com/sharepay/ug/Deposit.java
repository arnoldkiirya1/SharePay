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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        API_KEY = "";
        final Button getTokenBtn = findViewById(R.id.getTokenBtn);

        getTokenBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final AsyncTask execute = new AsyncTask() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    protected Object doInBackground(Object[] objects) {


                        UUID = java.util.UUID.randomUUID().toString();
                        PHONE_NUMBER = findViewById(R.id.phone_number);
                        AMOUNT = findViewById(R.id.amount);

                        try {
                            CREATION_RESPONSE_CODE = createUser.userCreation(PRIMARY_KEY, CONTENT_TYPE, UUID);
                            if (CREATION_RESPONSE_CODE == 400) {
                                return "INVALID_DATA";
                            } else if (CREATION_RESPONSE_CODE == 500) {
                                return "INTERNAL_SERVER_ERROR";
                            } else if (CREATION_RESPONSE_CODE == 201 || CREATION_RESPONSE_CODE == 409) {
                                TARGET_ENVIRONMENT = getUserInformation.userInformation(PRIMARY_KEY, CONTENT_TYPE, UUID);
                                API_KEY = getUserApiKey.userApiKey(PRIMARY_KEY, CONTENT_TYPE, UUID);
                                AUTH_KEY = Base64.encodeToString((UUID.concat(":").concat(API_KEY)).getBytes(), Base64.NO_WRAP);
                                USER_TOKEN = getToken.theToken(PRIMARY_KEY, AUTH_KEY);
                                if (!USER_TOKEN.equals("INTERNAL_SERVER_ERROR") && !USER_TOKEN.equals("ERROR") && !USER_TOKEN.equals("UnAuthorized")) {
                                    requestToPay.getRequestToPay(PRIMARY_KEY, CONTENT_TYPE, UUID, USER_TOKEN, TARGET_ENVIRONMENT, AMOUNT.getText().toString(), PHONE_NUMBER.getText().toString());
                                    tmp = getRequestToPayStatus.getStatus(PRIMARY_KEY, CONTENT_TYPE, TARGET_ENVIRONMENT, UUID, USER_TOKEN);

                                    while (true) {
                                        Handler handler = new Handler(Looper.getMainLooper());
                                        handler.post(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), tmp, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        SystemClock.sleep(60000);

                                        requestToPay.getRequestToPay(PRIMARY_KEY, CONTENT_TYPE, UUID, USER_TOKEN, TARGET_ENVIRONMENT, AMOUNT.getText().toString(), PHONE_NUMBER.getText().toString());
                                        tmp =getRequestToPayStatus.getStatus(PRIMARY_KEY, CONTENT_TYPE, TARGET_ENVIRONMENT, UUID, USER_TOKEN);

                                        getTokenAction();
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
                execute.execute();
            }
        });

    }

    public void getTokenAction() {
        // Open Success Activity
        Intent i = new Intent(this, Success.class);
        startActivity(i);
    }




}
