package com.sharepay.ug;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sharepay.ug.Model.Group;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateGroup extends AppCompatActivity {

    private TextView resultTextView;
    String publicGroupId;
    private EditText group_name, desc, target_fee, admin_cost;
    String selectedOption;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        // Create a ProgressDialog to show while processing
        progressDialog = new ProgressDialog(CreateGroup.this);
        progressDialog.setMessage("Setting up Group...");
        progressDialog.setCancelable(false);

        resultTextView = findViewById(R.id.textView12);
        group_name = findViewById(R.id.g_name);
        admin_cost = findViewById(R.id.admin_cost);

        desc = findViewById(R.id.desc);
        target_fee = findViewById(R.id.target);

        RadioGroup radioGroup = findViewById(R.id.services);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);

                if (selectedRadioButton != null) {
                    selectedOption = selectedRadioButton.getText().toString();
                    // Now, 'selectedOption' contains the text of the selected RadioButton.
                }
            }
        });

        // Find the button by its ID
        Button createbtn = findViewById(R.id.button);

        // Set an OnClickListener for the button
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create User
                createGroup();
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

    public void createGroup() {



        if(target_fee.getText().toString().isEmpty()){
            // Display an error message
            Toast.makeText(CreateGroup.this, "Enter your contribution!", Toast.LENGTH_LONG).show();
        } else {

            progressDialog.show();

            // Create a new group
            Group newGroup = new Group(
                    group_name.getText().toString(),
                    "icon_url",
                    MainActivity.userPhoneNumber,
                    desc.getText().toString(),
                    selectedOption,
                    target_fee.getText().toString(),
                    "active",
                    MainActivity.userFirstName + ", created the SharePay group"
            );

            // Create a Gson instance
            Gson gson = new Gson();

            // Serialize the Group object to a JSON string
            String jsonGroup = gson.toJson(newGroup);

            // Now you can print the JSON string
            System.out.println(jsonGroup);

            // Create a Retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://34.151.68.80/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create an instance of the API interface
            Api api = retrofit.create(Api.class);

            // Call the createGroup API method
            Call<ResponseBody> createGroupCall = api.createGroup(newGroup);

            createGroupCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        // Group creation was successful

                        try {
                            String responseBody = response.body().string(); // Convert response to string
                            JSONObject jsonObject = new JSONObject(responseBody);
                            int groupId = jsonObject.optInt("group_id");

                            // Store the group ID as a public string
                            publicGroupId = String.valueOf(groupId);

                            // Handle the add members
                            Intent intent = new Intent(CreateGroup.this, ViewGroupDetails.class);
                            intent.putExtra("publicGroupId", publicGroupId);
                            intent.putExtra("groupName", group_name.getText().toString());
                            intent.putExtra("admin_cost", admin_cost.getText().toString());

                            progressDialog.dismiss();

                            startActivity(intent);

                            finish();

                            // Perform any further actions, such as navigating to another activity
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            resultTextView.setText("Failed to create a new group.");
                            resultTextView.setTextColor(Color.RED);
                        }
                    } else {
                        // Handle the error response


                        resultTextView.setText("Failed to create a new group. Error: ");
                        resultTextView.setTextColor(Color.RED);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Handle network failure and log the error
                    Log.e("NetworkError", "Network request failed: " + t.getMessage());
                    resultTextView.setText("Network request failed.");
                    resultTextView.setTextColor(Color.RED);
                }
            });
        }
    }
}