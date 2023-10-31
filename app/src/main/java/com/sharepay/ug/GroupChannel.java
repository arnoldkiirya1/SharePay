package com.sharepay.ug;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sharepay.ug.Adapters.MessageAdapter;
import com.sharepay.ug.Collection.getRequestToPayStatus;
import com.sharepay.ug.Collection.getUserInformation;
import com.sharepay.ug.Collection.requestToPay;
import com.sharepay.ug.Model.Messages;
import com.sharepay.ug.Model.User;
import com.sharepay.ug.Sandbox_User.createUser;
import com.sharepay.ug.Sandbox_User.getToken;
import com.sharepay.ug.Sandbox_User.getUserApiKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GroupChannel extends AppCompatActivity {


    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Messages> messages;
    private int currentUserId; // Set the current user's ID
    private Api api;

    // Static variables for configuration and data
    private static String PRIMARY_KEY = "4f3293fc5f404e7097e90e0b4a022f32";
    private static String CONTENT_TYPE = "application/json";
    private static String UUID;
    private static TextView statusMsg;
    private int CREATION_RESPONSE_CODE;
    private TextView resultTextView, groupnameTextView, targetfeeTextview, percentageTextView, billTextview;
    private String TARGET_ENVIRONMENT;
    private static com.sharepay.ug.Sandbox_User.getUserApiKey getUserApiKey = new getUserApiKey();
    private String API_KEY, USER_TOKEN, AUTH_KEY;
    public String tmp;
    ProgressDialog progressDialog;

    EditText amount;
    TextView alertMsg;

    Button BillButton;

    Button openDialogButton;

    double balance_got;

    public static  String groupname, recent_transaction, admin_id, group_id ,bill, target_fee, collection_share, amount_topay, status ;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_channel);

        // Retrieve the phone number from the Intent
        groupname = getIntent().getStringExtra("group_name");
        recent_transaction = getIntent().getStringExtra("recent_transaction");
        admin_id = getIntent().getStringExtra("admin_id");
        group_id = getIntent().getStringExtra("group_id");
        bill = getIntent().getStringExtra("bill");
        target_fee = getIntent().getStringExtra("target_fee");
        collection_share = getIntent().getStringExtra("collection_share");
        status = getIntent().getStringExtra("status");

        calculateBalance(MainActivity.userPhoneNumber,group_id);


        resultTextView = findViewById(R.id.textView5);
        alertMsg = findViewById(R.id.textView29);


        percentageTextView = findViewById(R.id.textView10);

        groupnameTextView = findViewById(R.id.textView8);
        groupnameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the add members
                Intent intent = new Intent(GroupChannel.this, ViewGroupDetails.class);
                intent.putExtra("publicGroupId", group_id);
                intent.putExtra("groupName", groupname);
                intent.putExtra("admin_cost", "");

                startActivity(intent);

            }
        });

        targetfeeTextview = findViewById(R.id.textView30);
        billTextview = findViewById(R.id.textView9);
        openDialogButton = findViewById(R.id.depositBtn);
        BillButton = findViewById(R.id.paybill);

        if (!status.equals("active")){
            openDialogButton.setVisibility(View.GONE);
            targetfeeTextview.setVisibility(View.GONE);
            BillButton.setVisibility(View.GONE);

        }else{

            targetfeeTextview.setText("Group's Target Fee: Ugx "+target_fee);
        }

        groupnameTextView.setText(groupname);

        billTextview.setText(bill);

        //Load Messages
        recyclerView = findViewById(R.id.messagelist);
        messages = new ArrayList<>();


        // Set up the RecyclerView with the custom adapter
        messageAdapter = new MessageAdapter(this, messages, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);


        // Fetch messages from the server
        fetchMessages(group_id);

        //Get collection percentage
        calculatePercentage();

        // Scroll to the last item
        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);



        // Create a ProgressDialog to show while processing
        progressDialog = new ProgressDialog(GroupChannel.this);
        progressDialog.setMessage("Processing Payment...");
        progressDialog.setCancelable(false);

        ImageView gobackButton = findViewById(R.id.imageView2);

        //Handle Continue button click
        gobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Close the Create group Activity
                finish();
            }
        });


        // Inflate the dialog XML layout
        View dialogView = LayoutInflater.from(GroupChannel.this).inflate(R.layout.pay_dialog, null);

        // Create an AlertDialog for the dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GroupChannel.this);
        dialogBuilder.setView(dialogView);

        amount = dialogView.findViewById(R.id.fee);
        Button dialogButton = dialogView.findViewById(R.id.paybtn);

        //Empty by default
        //amount.setText("");

        final AlertDialog dialog = dialogBuilder.create();

        openDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Initialize variables
                API_KEY = "";


                dialog.show();

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(Double.parseDouble(amount.getText().toString()) > Double.parseDouble(collection_share)){
                            // Display an error message
                            Toast.makeText(GroupChannel.this, "Amount greater than your collection!", Toast.LENGTH_LONG).show();
                        } else {

                            progressDialog.show();

                            // Handle the click of the button in the dialog
                            String AMOUNT_TOPAY = amount.getText().toString();
                            amount_topay = amount.getText().toString();
                            String PHONE_NUMBER = MainActivity.userPhoneNumber; // Link this to the user


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
                                                requestToPay.getRequestToPay(PRIMARY_KEY, CONTENT_TYPE, UUID, USER_TOKEN, TARGET_ENVIRONMENT, AMOUNT_TOPAY, PHONE_NUMBER);
                                                tmp = getRequestToPayStatus.getStatus(PRIMARY_KEY, CONTENT_TYPE, TARGET_ENVIRONMENT, UUID, USER_TOKEN);

                                                //Get new balance
                                                calculateBalance(MainActivity.userPhoneNumber, group_id);

                                                //upload the data to group wallet
                                                uploadpayments();

                                                dialog.dismiss();


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
                                    // Show Success Message
                                    ShowSuccessDialog();
                                }
                            };
                            execute.execute(); // Execute the AsyncTask
                        }


                    }
                });


            }
        });

        // Open Bill Payments

        BillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the new Intent to start a new activity
                Intent intent = new Intent(GroupChannel.this, PayBills.class);
                startActivity(intent);
            }
        });



    }


    // Method to push payment details to the group wallet

    public void uploadpayments() {

        // Define the URL for the API endpoint
        String url = "http://34.151.68.80/v1/createMessage.php"; // Modify the URL with your endpoint

        // Calculate the balance
        double balanceFee =  balance_got - Double.parseDouble(amount_topay);
        System.out.println("Try:"+balance_got);

        // Create a StringRequest to send the payment data as POST parameters
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        calculateBalance(MainActivity.userPhoneNumber,group_id);

                        // Fetch messages from the server
                        fetchMessages(group_id);

                        System.out.println("Upload"+response);
                        // You can implement additional logic here upon success
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors and display an error message
                        if (error.networkResponse != null) {
                            String errorResponse = new String(error.networkResponse.data);
                            resultTextView.setText("Error: " + errorResponse);
                        } else {
                            resultTextView.setText("Network request failed");
                        }
                        // You can implement additional error handling logic here
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", MainActivity.userPhoneNumber); // Replace with the actual user ID
                params.put("group_id", group_id); // Replace with the actual group ID
                params.put("name", MainActivity.userFirstName+" "+ MainActivity.userLastName);
                params.put("amount_deposited", amount_topay); // Replace with the actual amount
                params.put("balance", String.valueOf(balance_got)); // Replace with the actual balance
                params.put("timestamp", getCurrentTime()); // Set the current timestamp
                return params;
            }
        };

        // Add the request to the Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void fetchMessages(String group_id) {

        // Define the base URL for the API endpoint
        String baseUrl = "http://34.151.68.80/v1/getMessages.php";

        // Create the full URL with the group_id as a query parameter
        String url = baseUrl + "?group_id=" + group_id;

        // Create a StringRequest to send a GET request to the API endpoint
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            messages.clear(); // Clear the existing messages

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject messageObject = jsonArray.getJSONObject(i);

                                // Parse the message data from the JSON object
                                String userId = messageObject.getString("user_id");
                                String name = messageObject.getString("name");
                                String groupId = messageObject.getString("group_id");
                                double amountDeposited = messageObject.getDouble("amount_deposited");
                                double balance = messageObject.getDouble("balance");
                                String timestamp = messageObject.getString("timestamp");

                                // Create a Messages object with the parsed data
                                Messages message = new Messages();
                                message.setUserId(userId);
                                message.setUserName(name);
                                message.setGroupId(groupId);
                                message.setAmountDeposited(amountDeposited);
                                message.setBalance(balance);
                                message.setTimestamp(String.valueOf(timestamp));

                                // Add the message to the list
                                messages.add(message);
                            }

                            // Update the RecyclerView
                            messageAdapter.notifyDataSetChanged();

                            // Scroll to the last item
                            recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors and display an error message
                        if (error.networkResponse != null) {
                            String errorResponse = new String(error.networkResponse.data);
                            resultTextView.setText("Error: " + errorResponse);
                        } else {
                            resultTextView.setText("Network request failed.");
                        }
                    }
                }
        );

        // Add the request to the Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void ShowSuccessDialog() {
        // Create a Dialog instance
        Dialog customDialog = new Dialog(GroupChannel.this);

        // Set the custom dialog layout
        customDialog.setContentView(R.layout.successful_pay);

        // Adjust the dialog dimensions and gravity
        Window window = customDialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
        }

        // Find the LottieAnimationView in the custom dialog layout
        LottieAnimationView animationView = customDialog.findViewById(R.id.lottieAnimationView);

        // Set the Lottie animation
        animationView.setAnimation(R.raw.done);
        animationView.playAnimation();

        // Show the dialog
        customDialog.show();

        // Use a Handler to dismiss the dialog after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customDialog.isShowing()) {
                    calculatePercentage();
                    customDialog.dismiss();
                }
            }
        }, 5000); // 5000 milliseconds (5 seconds)
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String formattedTime = timeFormat.format(calendar.getTime());
        return formattedTime;
    }

    private void calculateBalance(final String user_id, final String group_id) {
        String url = "http://34.151.68.80/v1/calculateBalance.php"; // Replace with your API endpoint

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            balance_got = jsonResponse.getDouble("balance");
                            System.out.println("Try:"+balance_got);

                            double deposited = Double.parseDouble(collection_share) - balance_got;
                            // Handle the balance value as needed
                            if (!status.equals("active")){

                                resultTextView.setText(recent_transaction);
                                alertMsg.setText("Bill Payment Successful");
                                BillButton.setVisibility(View.GONE);

                            }else{

                                resultTextView.setText("Your deposit: Ugx" + deposited+" / "+collection_share);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            if (!status.equals("active")){

                                resultTextView.setText(recent_transaction);

                            }else{

                                resultTextView.setText("Your deposit: Ugx " + "0.0" +" / "+collection_share);
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors and display an error message
                        if (error.networkResponse != null) {
                            String errorResponse = new String(error.networkResponse.data);
                            resultTextView.setText("Error: " + errorResponse);
                        } else {
                            resultTextView.setText("Network request failed.");
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("group_id", group_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void calculatePercentage() {
        // Define the URL for the API endpoint
        String url = "http://34.151.68.80/v1/calculatePercentage.php"; // Replace with your API endpoint

        // Create a StringRequest to send a POST request to the API endpoint
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        try {
                            double percentage = Double.parseDouble(response);
                            // Now, 'percentage' contains the calculated percentage
                            percentageTextView.setText(String.format("%.1f", percentage) + "%");
                            if(percentage>=100.0){

                                openDialogButton.setVisibility(View.GONE);
                                if (!status.equals("active")){

                                    BillButton.setVisibility(View.GONE);

                                } else{
                                    BillButton.setVisibility(View.VISIBLE);
                                }

                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            resultTextView.setText("Error: Invalid response format.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors and display an error message
                        if (error.networkResponse != null) {
                            String errorResponse = new String(error.networkResponse.data);
                            resultTextView.setText("Error: " + errorResponse);
                        } else {
                            resultTextView.setText("Network request failed.");
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("group_id", String.valueOf(group_id)); // Replace 'group_id' with the actual group ID
                return params;
            }
        };

        // Add the request to the Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}