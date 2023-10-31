package com.sharepay.ug.Remittance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetCashTransferStatus {

    public static String getStatus(String PRIMARY_KEY, String REFERENCE_ID, String USER_TOKEN, String TARGET_ENVIRONMENT) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        Request request = new Request.Builder()
                .url("https://sandbox.momodeveloper.mtn.com/remittance/v2_0/cashtransfer/" + REFERENCE_ID)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + USER_TOKEN)
                .addHeader("X-Target-Environment", TARGET_ENVIRONMENT)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            // Parse the JSON response
            JSONObject responseObject = new JSONObject(response.body().string());

            // Extract and print the relevant data
            String status = responseObject.optString("status");

            return "Cash Transfer Status: " + status;
        } else {
            // Handle the case where the GET request was not successful
            String errorMessage = "GET request failed with status code: " + response.code();
            System.out.println(errorMessage);
            return errorMessage;
        }
    }
}