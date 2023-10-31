package com.sharepay.ug;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.sharepay.ug.Adapters.GroupMemberAdapter;
import com.sharepay.ug.Adapters.MessageAdapter;
import com.sharepay.ug.Model.GroupMember;
import com.sharepay.ug.Model.GroupOption;
import com.sharepay.ug.Model.UserOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddMembers extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    String groupId, admin_cost2;
    String phoneNumber;
    int Status = 0;

    private RecyclerView recyclerView;
    private GroupMemberAdapter adapter;
    private List<GroupOption> groupMembers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        // Retrieve the phone number from the Intent
        groupId = getIntent().getStringExtra("publicGroupId");
        admin_cost2 = getIntent().getStringExtra("admin_cost1");

        System.out.println("Admin Costs2 "+admin_cost2);

        EditText userNameEditText = findViewById(R.id.g_name2);
        EditText collectionShareEditText = findViewById(R.id.g_name3);

        //Add admin to group
        if(!admin_cost2.isEmpty()){
            adddmin();
        }

        ImageView gobackButton = findViewById(R.id.imageView6);

        //Handle Continue button click
        gobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Close the Create group Activity
                finish();
            }
        });


        List<String> searchResults = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, searchResults);
        autoCompleteTextView = findViewById(R.id.g_name2);
        autoCompleteTextView.setAdapter(adapter);


        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This method is not used, but it's part of TextWatcher
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsersByPhoneNumber(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This method is not used, but it's part of TextWatcher
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) parent.getItemAtPosition(position);

                // Split the selected option into phone number, first name, and last name
                String[] parts = selectedOption.split(": ");
                if (parts.length == 2) {
                    phoneNumber = parts[0];
                    String name = parts[1];

                    // Split the name into first name and last name if needed
                    String[] nameParts = name.split(" ");
                    String firstName = nameParts[0];
                    String lastName = nameParts.length > 1 ? nameParts[1] : "";

                    autoCompleteTextView.setText(firstName+" "+lastName);

                    // You now have access to all three pieces of information

                }
            }
        });

        // Find the button by its ID
        Button addBtn = findViewById(R.id.button3);

        // Set an OnClickListener for the button
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add User

                // Create a new GroupMember instance
                GroupMember newGroupMember = new GroupMember(groupId, userNameEditText.getText().toString(), phoneNumber, collectionShareEditText.getText().toString());

                // Define the URL for the API endpoint
                String url = "http://34.151.68.80/v1/addGroupMember.php";

                // Create a request queue using Volley
                RequestQueue queue = Volley.newRequestQueue(AddMembers.this);

                // Create a `StringRequest` to send form data
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        Toast.makeText(AddMembers.this, "Group member added successfully", Toast.LENGTH_SHORT).show();

                        // Go back to view group Members
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors and display an error message
                        if (error.networkResponse != null) {
                            String errorResponse = new String(error.networkResponse.data);
                            Toast.makeText(AddMembers.this, "Error: " + errorResponse, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddMembers.this, "Network request failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Define the form parameters
                        Map<String, String> params = new HashMap<>();
                        params.put("group_id", newGroupMember.getGroupId());
                        params.put("user_name", newGroupMember.getUserName());
                        params.put("user_id", newGroupMember.getUserId());
                        params.put("collection_share", newGroupMember.getCollectionShare());
                        return params;
                    }
                };

                // Add the StringRequest to the request queue
                queue.add(request);

            }
        });


    }

    private void adddmin(){

        // Add User
        String name = MainActivity.userFirstName +" "+ MainActivity.userLastName;
        // Create a new GroupMember instance
        GroupMember newGroupMember = new GroupMember(groupId, name, MainActivity.userPhoneNumber, admin_cost2);

        // Define the URL for the API endpoint
        String url = "http://34.151.68.80/v1/addGroupMember.php";

        // Create a request queue using Volley
        RequestQueue queue = Volley.newRequestQueue(AddMembers.this);

        // Create a `StringRequest` to send form data
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Handle the response from the server

                System.out.println("Admin Added");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle network errors and display an error message
                if (error.networkResponse != null) {
                    String errorResponse = new String(error.networkResponse.data);
                    Toast.makeText(AddMembers.this, "Error: " + errorResponse, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddMembers.this, "Network request failed", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Define the form parameters
                Map<String, String> params = new HashMap<>();
                params.put("group_id", newGroupMember.getGroupId());
                params.put("user_name", newGroupMember.getUserName());
                params.put("user_id", newGroupMember.getUserId());
                params.put("collection_share", newGroupMember.getCollectionShare());
                return params;
            }
        };

        // Add the StringRequest to the request queue
        queue.add(request);

    }

    private void searchUsersByPhoneNumber(String userphoneNumber) {
        // Define the URL for the API endpoint
        String url = "http://34.151.68.80/v1/searchUsers.php?phone=" + userphoneNumber;

        // Create a request queue using Volley
        RequestQueue queue = Volley.newRequestQueue(this);

        // Define a JSON request to search for users
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<String> searchResults = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject user = response.getJSONObject(i);
                                String phone = user.getString("phone");
                                String firstName = user.getString("first_name");
                                String lastName = user.getString("last_name");

                                searchResults.add(phone + ": " + firstName + " " + lastName);
                                Log.d("SearchResults", "Phone: " + phone + ", Name: " + firstName + " " + lastName);
                            }

                            // Update the existing adapter with the new search results
                            ArrayAdapter<String> adapter = (ArrayAdapter<String>) autoCompleteTextView.getAdapter();
                            adapter.clear();
                            adapter.addAll(searchResults);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors and display an error message
                        Log.e("ApiError", "API Request Failed: " + error.toString());
                    }
                }
        );

        // Add the JSON request to the request queue
        queue.add(request);
    }


}