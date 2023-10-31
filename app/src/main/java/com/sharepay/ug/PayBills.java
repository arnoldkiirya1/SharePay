package com.sharepay.ug;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sharepay.ug.Collection.getRequestToPayStatus;
import com.sharepay.ug.Collection.getUserInformation;
import com.sharepay.ug.Collection.requestToPay;
import com.sharepay.ug.Remittance.CashTransfer;
import com.sharepay.ug.Remittance.GetCashTransferStatus;
import com.sharepay.ug.Sandbox_User.createUser;
import com.sharepay.ug.Sandbox_User.getToken;
import com.sharepay.ug.Sandbox_User.getUserApiKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PayBills extends AppCompatActivity {

    // Static variables for configuration and data
    private static String PRIMARY_KEY = "4f3293fc5f404e7097e90e0b4a022f32";
    private static String CONTENT_TYPE = "application/json";
    private static String UUID;
    private static TextView statusMsg;
    private int CREATION_RESPONSE_CODE;
    private TextView resultTextView;
    private String TARGET_ENVIRONMENT;
    private static com.sharepay.ug.Sandbox_User.getUserApiKey getUserApiKey = new getUserApiKey();
    private String API_KEY, USER_TOKEN, AUTH_KEY;
    public String tmp;
    EditText ben_phone, bank_acct;
    ProgressDialog progressDialog;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bills);

        resultTextView = findViewById(R.id.errormsg);
        ben_phone = findViewById(R.id.f_name);
        bank_acct = findViewById(R.id.l_name);



        // Open the Yaka Activity
        ImageView yaka = findViewById(R.id.imageView4);

        yaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                Intent intent = new Intent(PayBills.this, PayYaka.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView gobackButton = findViewById(R.id.imageView6);

        //Handle Continue button click
        gobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Close the Create group Activity
                finish();
            }
        });

        // Create a ProgressDialog to show while processing
        progressDialog = new ProgressDialog(PayBills.this);
        progressDialog.setMessage("Processing Payment...");
        progressDialog.setCancelable(false);


        // Declare the additional parameters needed for CashTransfer
        String CURRENCY = "EUR";
        String PAYEE_PARTY_ID = "payee_party_id";
        String PAYEE_PARTY_ID_TYPE = "MSISDN";
        String EXTERNAL_ID = "external_id";
        String ORIGINATING_COUNTRY = "US";
        String AMOUNT_TOPAY = "200000"; // You can use the same amount
        String ORIGINAL_AMOUNT = "200000"; // You can use the same amount
        String ORIGINAL_CURRENCY = "EUR";
        String PAYER_MESSAGE = "Please Note";
        String PAYEE_NOTE = "Confirm To Pay";
        String PAYER_IDENTIFICATION_TYPE = "MSISDN";
        String PAYER_IDENTIFICATION_NUMBER = "1234567890";
        String PAYER_IDENTITY = "payerIdentity";
        String PAYER_FIRST_NAME = "John";
        String PAYER_SURNAME = "Doe";
        String PAYER_LANGUAGE_CODE = "en";
        String PAYER_EMAIL = "johndoe@example.com";
        String PAYER_MSISDN = "1234567890";
        String PAYER_GENDER = "Male";

        // Declare the parameters needed for GetCashTransferStatus
        String REFERENCE_ID = "your_reference_id"; // Replace with the actual reference ID

        // Initialize variables
        API_KEY = "";

        // Open Bill Payments
        Button finalPay = findViewById(R.id.button);

        finalPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!ben_phone.getText().toString().isEmpty() || !bank_acct.getText().toString().isEmpty() ) {

                    progressDialog.show();

                    // Define an AsyncTask for background processing
                    final AsyncTask execute = new AsyncTask() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        protected Object doInBackground(Object[] objects) {

                            // Generate a UUID and get phone number and amount from UI
                            UUID = java.util.UUID.randomUUID().toString();


                            try {
                                // Create a user and handle potential response codes
                                CREATION_RESPONSE_CODE = createUser.userCreation(PRIMARY_KEY, CONTENT_TYPE, UUID);
                                if (CREATION_RESPONSE_CODE == 400) {

                                    resultTextView.setText("Invalid Input Data");

                                } else if (CREATION_RESPONSE_CODE == 500) {

                                    resultTextView.setText("Internal Server Error");

                                } else if (CREATION_RESPONSE_CODE == 201 || CREATION_RESPONSE_CODE == 409) {
                                    // Obtain user information, API key, and authentication key
                                    TARGET_ENVIRONMENT = getUserInformation.userInformation(PRIMARY_KEY, CONTENT_TYPE, UUID);
                                    API_KEY = getUserApiKey.userApiKey(PRIMARY_KEY, CONTENT_TYPE, UUID);
                                    AUTH_KEY = Base64.encodeToString((UUID.concat(":").concat(API_KEY)).getBytes(), Base64.NO_WRAP);
                                    USER_TOKEN = getToken.theToken(PRIMARY_KEY, AUTH_KEY);
                                    if (!USER_TOKEN.equals("INTERNAL_SERVER_ERROR") && !USER_TOKEN.equals("ERROR") && !USER_TOKEN.equals("UnAuthorized")) {
                                        // Perform payment request and get status
                                        CashTransfer.performCashTransfer(
                                                PRIMARY_KEY, CONTENT_TYPE, UUID, USER_TOKEN, TARGET_ENVIRONMENT, AMOUNT_TOPAY, CURRENCY,
                                                PAYEE_PARTY_ID, PAYEE_PARTY_ID_TYPE, EXTERNAL_ID, ORIGINATING_COUNTRY,
                                                ORIGINAL_AMOUNT, ORIGINAL_CURRENCY, PAYER_MESSAGE, PAYEE_NOTE,
                                                PAYER_IDENTIFICATION_TYPE, PAYER_IDENTIFICATION_NUMBER, PAYER_IDENTITY,
                                                PAYER_FIRST_NAME, PAYER_SURNAME, PAYER_LANGUAGE_CODE, PAYER_EMAIL,
                                                PAYER_MSISDN, PAYER_GENDER
                                        );
                                        tmp = GetCashTransferStatus.getStatus(
                                                PRIMARY_KEY, REFERENCE_ID, USER_TOKEN, TARGET_ENVIRONMENT
                                        );

                                        if (!ben_phone.getText().toString().isEmpty()) {

                                            String msg = "Dear Customer, payment of Ugx " + GroupChannel.target_fee + " for " + ben_phone.getText().toString() + ". Thank you for paying using SharePay.";
                                            updateGroupStatusAndTransaction(GroupChannel.group_id, "Paid", msg);
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(PayBills.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {

                                        }

                                        if (!bank_acct.getText().toString().isEmpty()) {
                                            String msg = "Dear Customer, payment of Ugx " + GroupChannel.target_fee + " to " + bank_acct.getText().toString() + "successful. Thank you for paying using SharePay.";
                                            updateGroupStatusAndTransaction(GroupChannel.group_id, "Paid", msg);
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(PayBills.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {

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

                        @Override
                        protected void onPostExecute(Object result) {
                            progressDialog.dismiss();
                        }
                    };
                    execute.execute(); // Execute the AsyncTask
                } else {
                    // Display an error message
                    Toast.makeText(PayBills.this, "Enter Recipient's details!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void updateGroupStatusAndTransaction(final String groupId, final String newStatus, final String newRecentTransaction) {
        String url = "http://34.151.68.80/v1/updateGroup.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            String message = jsonResponse.getString("message");
                            // Handle the success message as needed
                            Log.e("Volley Success", message); // Print the success response
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorResponse = new String(error.networkResponse.data);
                            Log.e("Volley Error", errorResponse); // Print the error response
                        } else {
                            Log.e("Volley Error", "Unknown error occurred");
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("group_id", groupId);
                params.put("status", newStatus);
                params.put("recent_transaction", newRecentTransaction); // Add recent_transaction
                return params;
            }
        };

        // Add the request to the Volley request queue
        Volley.newRequestQueue(PayBills.this).add(stringRequest);
    }
}