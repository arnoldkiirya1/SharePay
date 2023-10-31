package com.sharepay.ug;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class SignIn extends AppCompatActivity {

    private CountryCodePicker countryCodePicker;
    private EditText phoneNumberEditText;
    private Button continueButton;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        // Create a ProgressDialog to show while processing
        progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setMessage("Signing in...");
        progressDialog.setCancelable(false);

        countryCodePicker = findViewById(R.id.country_code);
        phoneNumberEditText = findViewById(R.id.phone_number);
        continueButton = findViewById(R.id.button);

        // Set the default country code to "UG" (Uganda)
        countryCodePicker.setCountryForNameCode("UG");


        //Handle Continue button click
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                String countryCode = countryCodePicker.getSelectedCountryCode();
                String phoneNumber = phoneNumberEditText.getText().toString();

                if (isValidPhoneNumber(phoneNumber)) {
                    // Phone number is valid, proceed with the next steps
                    String completePhoneNumber = countryCode + phoneNumber;
                    System.out.println("Phone:"+completePhoneNumber);

                    // Passing `completePhoneNumber` to the next OTP activity

                    Intent intent = new Intent(SignIn.this, Otp.class);
                    intent.putExtra("phone_number", completePhoneNumber);


                    progressDialog.dismiss();
                    startActivity(intent);

                    // Close the SignInActivity
                    finish();

                } else {
                    // Display an error message
                    Toast.makeText(SignIn.this, "Invalid phone number", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Validate that the phone number starts with 7 and has exactly 9 digits
        return phoneNumber.matches("^7\\d{8}$");
    }


}