package com.sharepay.ug;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sharepay.ug.Adapters.GroupMemberAdapter;
import com.sharepay.ug.Adapters.GroupRequestsAdapter;
import com.sharepay.ug.Adapters.GroupsAdapter;
import com.sharepay.ug.Model.GroupList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    public static String greetTime;

    public static String userPhoneNumber, userFirstName, userLastName;
    TextView firstname_view;

    @SuppressLint({"StaticFieldLeak", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gettime();

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Initialize your database helper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        if (dbHelper.isUsersTableEmpty()) {

            // Load the SignInActivity if the "users" table is empty
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            finish(); // Finish the current activity to prevent returning to it

        } else {

            // Retrieve and store the phone number and first name
            userPhoneNumber = dbHelper.getPhoneNumber();
            userFirstName = dbHelper.getFirstName();
            userLastName = dbHelper.getLastName();

            getGroups(userPhoneNumber);

            // Add the first name to Main Activity
            firstname_view = findViewById(R.id.textView2);

            firstname_view.setText(greetTime+userFirstName+"!");

        }

        // View Requests or Refresh page
        Button joinGroupButton = findViewById(R.id.button5);

        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("pressed"+joinGroupButton.getText().toString());
                // Handle the click event here
                if(joinGroupButton.getText().toString().equals("Refresh")) {
                    getGroupRequests(userPhoneNumber);
                } else{
                    getGroups(userPhoneNumber);
                }
            }
        });

        // Open the Create Group Activity
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                Intent intent = new Intent(MainActivity.this, CreateGroup.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_navigation_item_groups:
                        // Handle the "SharePay" menu item click
                        getGroups(userPhoneNumber);
                        return true;

                    case R.id.bottom_navigation_item_history:

                        // Handle the "Request" menu item click
                        getGroupRequests(userPhoneNumber);

                        return true;

                    case R.id.bottom_navigation_item_profile:
                        // Handle the "Account" menu item click
                        ShowProfile();
                        return true;
                }
                return false;
            }
        });




    }

    private void ShowHistory() {
        // Create a Dialog instance
        Dialog customDialog = new Dialog(MainActivity.this);

        // Set the custom dialog layout
        customDialog.setContentView(R.layout.transaction_history);

        // Adjust the dialog dimensions and gravity
        Window window = customDialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);  // Set the height to WRAP_CONTENT
            window.setGravity(Gravity.BOTTOM);  // Start from the bottom
        }

        // Show the dialog
        customDialog.show();

    }

    private void ShowProfile() {
        // Create a Dialog instance
        Dialog customDialog = new Dialog(MainActivity.this);

        // Set the custom dialog layout
        customDialog.setContentView(R.layout.view_profile);

        // Adjust the dialog dimensions and gravity
        Window window = customDialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);  // Set the height to WRAP_CONTENT
            window.setGravity(Gravity.BOTTOM);  // Start from the bottom
        }

        // Find the TextView inside the dialog by its ID
        TextView name = customDialog.findViewById(R.id.textView23);
        TextView phone = customDialog.findViewById(R.id.chatMessage4);

        name.setText(userFirstName +" "+userLastName);
        phone.setText( "+"+userPhoneNumber);


        // Show the dialog
        customDialog.show();
    }

    private void getGroups(String user_id) {

        // Clear the current data and remove the existing adapter
        recyclerView.setAdapter(null);

        // Define the URL for the API endpoint.
        String url = "http://34.151.68.80/v1/getGroup.php?user_id=" + user_id;

        // Create a request queue using Volley.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Define a JSON request to get group members.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Process the JSON array
                            List<GroupList> groups = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject group = response.getJSONObject(i);
                                String group_name = group.getString("group_name");
                                String group_id = group.getString("group_id");
                                String recent_transaction = group.getString("recent_transaction");
                                String date_updated = group.getString("date_updated");
                                String admin_id = group.getString("admin_id");
                                String target_fee = group.getString("target_fee");
                                String bill = group.getString("bill");
                                String collection_share = group.getString("collection_share");
                                String status = group.getString("status");

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                Date date = dateFormat.parse(date_updated);
                                long timestamp = date.getTime();

                                groups.add(new GroupList(group_name, recent_transaction, formatTimestampToTimeAMPM(timestamp),admin_id,target_fee,bill, collection_share, group_id, status));
                            }

                            // Create an adapter and set it for your RecyclerView.
                            GroupsAdapter adapter = new GroupsAdapter(groups, new GroupsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(GroupList item) {
                                    // Handle the item click here
                                    // Start a new activity and pass the item's parameters
                                    Intent intent = new Intent(MainActivity.this, GroupChannel.class);
                                    intent.putExtra("group_name", item.getGroupName());
                                    intent.putExtra("recent_transaction", item.getRecentTransaction());
                                    intent.putExtra("admin_id", item.getAdminId());
                                    intent.putExtra("group_id", item.getGroupId());
                                    intent.putExtra("bill", item.getBill());
                                    intent.putExtra("target_fee", item.getTargetFee());
                                    intent.putExtra("collection_share", item.getCollectionShare());
                                    intent.putExtra("status", item.getStatus());
                                    // Add more parameters as needed
                                    startActivity(intent);

                                }
                            });


                            // Add new data
                            recyclerView.setAdapter(adapter);


                            Log.e("Counts", "API Request Failed: " + adapter.getItemCount());

                            // No Groups Found
                            TextView noChatsTextView = findViewById(R.id.textView24);
                            TextView noChatsDescTextView = findViewById(R.id.textView26);

                            Button joinGroupButton = findViewById(R.id.button5);

                            // Check if the RecyclerView's adapter is empty
                            if (adapter.getItemCount() == 0) {

                                noChatsTextView.setVisibility(View.VISIBLE);
                                noChatsDescTextView.setVisibility(View.VISIBLE);
                                joinGroupButton.setVisibility(View.VISIBLE);


                            } else {

                                noChatsTextView.setVisibility(View.GONE);
                                noChatsDescTextView.setVisibility(View.GONE);
                                joinGroupButton.setVisibility(View.GONE);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors and display an error message.
                        Log.e("Check API", "API Request Failed: " + error.toString());

                        // No Groups Found
                        TextView noChatsTextView = findViewById(R.id.textView24);
                        TextView noChatsDescTextView = findViewById(R.id.textView26);

                        Button joinGroupButton = findViewById(R.id.button5);

                        noChatsTextView.setText("No internet!");
                        noChatsDescTextView.setText("Check your internet connection");
                        joinGroupButton.setText("Refresh");

                        noChatsTextView.setVisibility(View.VISIBLE);
                        noChatsDescTextView.setVisibility(View.VISIBLE);
                        joinGroupButton.setVisibility(View.VISIBLE);

//                        Toast.makeText(MainActivity.this, "Failed to fetch group members: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the JSON request to the request queue.
        queue.add(request);
    }

    private void getGroupRequests(String user_id) {

        // Clear the current data and remove the existing adapter
        recyclerView.setAdapter(null);

        // Define the URL for the API endpoint.
        String url = "http://34.151.68.80/v1/fetchRequests.php?user_id=" + user_id;

        // Create a request queue using Volley.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Define a JSON request to get group members.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Process the JSON array
                            List<GroupList> groups = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject group = response.getJSONObject(i);
                                String group_name = group.getString("group_name");
                                String group_id = group.getString("group_id");
                                String recent_transaction = group.getString("recent_transaction");
                                String date_updated = group.getString("date_updated");
                                String admin_id = group.getString("admin_id");
                                String target_fee = group.getString("target_fee");
                                String bill = group.getString("bill");
                                String collection_share = group.getString("collection_share");
                                String status = group.getString("status");

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                Date date = dateFormat.parse(date_updated);
                                long timestamp = date.getTime();

                                groups.add(new GroupList(group_name, recent_transaction, formatTimestampToTimeAMPM(timestamp),admin_id,target_fee,bill, collection_share, group_id, status));
                            }

                            // Create an adapter and set it for your RecyclerView.
                            GroupRequestsAdapter adapter = new GroupRequestsAdapter(groups, new GroupRequestsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(GroupList item) {
                                    // Handle the item click here
                                    updateGroupMemberStatus(item.getGroupId(), userPhoneNumber);


                                }
                            });


                            // Add new data
                            recyclerView.setAdapter(adapter);


                            Log.e("Counts", "API Request Failed: " + adapter.getItemCount());

                            // No Groups Found
                            TextView noChatsTextView = findViewById(R.id.textView24);
                            TextView noChatsDescTextView = findViewById(R.id.textView26);

                            Button joinGroupButton = findViewById(R.id.button5);

                            // Check if the RecyclerView's adapter is empty
                            if (adapter.getItemCount() == 0) {

                                noChatsTextView.setVisibility(View.VISIBLE);
                                noChatsDescTextView.setVisibility(View.VISIBLE);
                                joinGroupButton.setVisibility(View.VISIBLE);


                            } else {

                                noChatsTextView.setVisibility(View.GONE);
                                noChatsDescTextView.setVisibility(View.GONE);
                                joinGroupButton.setVisibility(View.GONE);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors and display an error message.
                        Log.e("Check API", "API Request Failed: " + error.toString());

                        // No Groups Found
                        TextView noChatsTextView = findViewById(R.id.textView24);
                        TextView noChatsDescTextView = findViewById(R.id.textView26);

                        Button joinGroupButton = findViewById(R.id.button5);

                        noChatsTextView.setText("No internet!");
                        noChatsDescTextView.setText("Check your internet connection");
                        joinGroupButton.setText("Refresh");

                        noChatsTextView.setVisibility(View.VISIBLE);
                        noChatsDescTextView.setVisibility(View.VISIBLE);
                        joinGroupButton.setVisibility(View.VISIBLE);

//                        Toast.makeText(MainActivity.this, "Failed to fetch group members: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the JSON request to the request queue.
        queue.add(request);
    }

    public void gettime(){

        int hour = getHour();
        System.out.println("current hour is"+hour);

        if (hour >= 0 && hour < 12) greetTime = "Good Morning, ";
        else if (hour >= 12 && hour < 18) greetTime = "Good Afternoon, ";
        else if (hour >= 18 && hour < 24) greetTime = "Good Evening, ";

        System.out.println("current hour is"+greetTime);

    }
    private static int getHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static String formatTimestampToTimeAMPM(long timestamp) {
        try {
            // Create a Date object from the timestamp
            Date date = new Date(timestamp);

            // Format the Date object to a time string with AM/PM
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);
            return timeFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updateGroupMemberStatus(final String groupId, final String userId) {
        String url = "http://34.151.68.80/v1/updateGroupMember.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the response if needed
                            // Handle the success response
                            getGroupRequests(userPhoneNumber);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        error.printStackTrace();
                    }
                }) {
            // Add POST parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("group_id", String.valueOf(groupId));
                params.put("user_id", String.valueOf(userId));
                return params;
            }
        };

        // Add the request to the Volley request queue
        Volley.newRequestQueue(MainActivity.this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_item_groups);
    }


}
