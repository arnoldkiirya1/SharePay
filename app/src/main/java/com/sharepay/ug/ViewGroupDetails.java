package com.sharepay.ug;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sharepay.ug.Adapters.GroupMemberAdapter;
import com.sharepay.ug.Model.GroupOption;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ViewGroupDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    String groupId, groupName, admin_cost1;

    TextView groupnameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_details);

        // Retrieve the Group name from the Intent
        groupId = getIntent().getStringExtra("publicGroupId");
        groupName = getIntent().getStringExtra("groupName");
        admin_cost1 = getIntent().getStringExtra("admin_cost");

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groupnameTextView = findViewById(R.id.textView22);
        groupnameTextView.setText(groupName);

        // Call the method to get group members
        getGroupMembers(groupId);

        Button addMemberButton = findViewById(R.id.button6);
        ImageView addMemberButton1 = findViewById(R.id.imageView14);

        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewGroupDetails.this, AddMembers.class);
                intent.putExtra("publicGroupId", groupId);
                intent.putExtra("admin_cost1", admin_cost1);
                System.out.println("Admin Costs1 "+admin_cost1);
                startActivityForResult(intent, 1);

            }
        });

        ImageView gobackButton = findViewById(R.id.imageView10);

        //Handle Continue button click
        gobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Close the Create group Activity
                finish();
            }
        });

        addMemberButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewGroupDetails.this, AddMembers.class);
                intent.putExtra("admin_cost1", admin_cost1);
                intent.putExtra("publicGroupId", groupId);
                System.out.println("Admin Costs1 "+admin_cost1);
                startActivityForResult(intent, 1);

            }
        });

        Button close = findViewById(R.id.button4);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewGroupDetails.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    private void getGroupMembers(String groupId) {
        // Define the URL for the API endpoint.
        String url = "http://34.151.68.80/v1/getGroupMembers.php?group_id=" + groupId;

        // Create a request queue using Volley.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Define a JSON request to get group members.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Process the JSON array
                            List<GroupOption> groupMembers = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject member = response.getJSONObject(i);
                                String userName = member.getString("user_name");
                                String userId = "+"+member.getString("user_id");
                                String collectionShare = member.getString("collection_share");

                                groupMembers.add(new GroupOption(userName, userId, collectionShare));
                            }

                            // Create an adapter and set it for your RecyclerView.
                            GroupMemberAdapter adapter = new GroupMemberAdapter(groupMembers);
                            recyclerView.setAdapter(adapter);

                            // No Members Found
                            TextView noMemberTextView = findViewById(R.id.textView27);
                            TextView noMemberDescTextView = findViewById(R.id.textView28);
                            Button addMemberButton = findViewById(R.id.button6);

                            // Check if the RecyclerView's adapter is empty
                            if (adapter.getItemCount() == 0) {

                                noMemberTextView.setVisibility(View.VISIBLE);
                                noMemberDescTextView.setVisibility(View.VISIBLE);
                                addMemberButton.setVisibility(View.VISIBLE);


                            } else {

                                noMemberTextView.setVisibility(View.GONE);
                                noMemberDescTextView.setVisibility(View.GONE);
                                addMemberButton.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors and display an error message.
                        Log.e("Check API", "API Request Failed: " + error.toString());
                        Toast.makeText(ViewGroupDetails.this, "Failed to fetch group members: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the JSON request to the request queue.
        queue.add(request);
    }

    // Handle the result from Activity 2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // Check if the result is from Activity 2
            if (resultCode == RESULT_OK) {

                // Call the method to get group members
                getGroupMembers(groupId);
            }
        }
    }
}
