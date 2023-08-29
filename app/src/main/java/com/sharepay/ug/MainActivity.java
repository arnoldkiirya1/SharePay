package com.sharepay.ug;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import ci.bamba.regis.Environment;
import ci.bamba.regis.Collections;
import io.reactivex.functions.Consumer;

import android.annotation.SuppressLint;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create an instance of the Collections class
        Collections collections = new Collections(
                "your_base_url_here",
                Environment.SANDBOX, // Use the appropriate environment
                "your_subscription_key_here",
                "your_api_user_here",
                "your_api_key_here"
        );

        // Parameters for the requestToPay method
        float amount = 900;
        String currency = "EUR";
        String externalId = "201904141150";
        String payerPartyId = "0022505777777";
        String payerMessage = "This is your order 1234";
        String payeeNote = "Order 1234";

        // Make a requestToPay API call
        collections.requestToPay(amount, currency, externalId, payerPartyId, payerMessage, payeeNote)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String referenceId) {
                        // This function executes in case of success.
                        System.out.println("Reference ID: " + referenceId);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        // This function executes in case of an error.
                        System.err.println("Error making requestToPay: " + throwable.getMessage());
                    }
                });
    }
}



