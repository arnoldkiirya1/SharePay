package com.sharepay.ug;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharepay.ug.Model.User;

public class Otp extends AppCompatActivity {

    String phoneNumber;
    TextView msgTextview;
    private Button continueButton;
    ProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // Create a ProgressDialog to show while processing
        progressDialog = new ProgressDialog(Otp.this);
        progressDialog.setMessage("Verifying OTP...");
        progressDialog.setCancelable(false);

        // Retrieve the phone number from the Intent
        phoneNumber = getIntent().getStringExtra("phone_number");

         msgTextview = findViewById(R.id.textView7);
         // Show case the number:
        msgTextview.setText("Enter the code sent to +"+phoneNumber);

        continueButton = findViewById(R.id.button);

        //Handle Continue button click
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Passing `completePhoneNumber` to the next OTP activity


                Intent intent = new Intent(Otp.this, SetupProfile.class);
                intent.putExtra("phone_number", phoneNumber);

                startActivity(intent);

                // Close the SignInActivity
                finish();
            }
        });

        ImageView gobackButton = findViewById(R.id.imageView5);

        //Handle Continue button click
        gobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Passing `completePhoneNumber` to the next OTP activity


                Intent intent = new Intent(Otp.this, SignIn.class);
                startActivity(intent);

                // Close the SignInActivity
                finish();
            }
        });

    }


}