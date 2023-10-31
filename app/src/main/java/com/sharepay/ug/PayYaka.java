package com.sharepay.ug;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PayYaka extends AppCompatActivity {


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_yaka);

        // Create a ProgressDialog to show while processing
        progressDialog = new ProgressDialog(PayYaka.this);
        progressDialog.setMessage("Processing Payment...");
        progressDialog.setCancelable(false);

        TextView acctdets = findViewById(R.id.textView31);
        EditText acctname = findViewById(R.id.f_name2);
        EditText meter_number = findViewById(R.id.f_name);

        // Open the Yaka Activity
        Button procced = findViewById(R.id.button0);
        // Open the Yaka Activity
        Button pay = findViewById(R.id.button7);


        procced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here

                    acctdets.setVisibility(View.VISIBLE);
                    acctname.setText(MainActivity.userFirstName +" "+MainActivity.userLastName);
                    acctname.setVisibility(View.VISIBLE);
                    procced.setVisibility(View.GONE);
                    pay.setVisibility(View.VISIBLE);

            }
        });



        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                if (!meter_number.getText().toString().isEmpty()) {
                    progressDialog.show();
                    String msg = "Dear Customer, payment of Ugx " + GroupChannel.target_fee + " for a/c " + meter_number.getText().toString() + " received by UMEME. Token no. 07641895640132697006, Units 10.6 KWh. Thank you for paying using SharePay.";
                    updateGroupStatusAndTransaction(GroupChannel.group_id, "Paid", msg);

                    // Use a Handler to delay the finish() and progressDialog.dismiss()
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Intent intent = new Intent(PayYaka.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }, 5000); // 5000 milliseconds = 5 seconds
                } else {
                    // Display an error message
                    Toast.makeText(PayYaka.this, "Meter number missing!", Toast.LENGTH_LONG).show();
                }
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
    }

    // Method to push payment details to the group wallet

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
        Volley.newRequestQueue(PayYaka.this).add(stringRequest);
    }


}