package com.sharepay.ug.Remittance;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.sharepay.ug.Sandbox_User.getToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CashTransfer extends getToken {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static int performCashTransfer(String PRIMARY_KEY, String CONTENT_TYPE, String UUID, String USER_TOKEN, String TARGET_ENVIRONMENT, String AMOUNT, String CURRENCY, String PAYEE_PARTY_ID, String PAYEE_PARTY_ID_TYPE, String EXTERNAL_ID, String ORIGINATING_COUNTRY, String ORIGINAL_AMOUNT, String ORIGINAL_CURRENCY, String PAYER_MESSAGE, String PAYEE_NOTE, String PAYER_IDENTIFICATION_TYPE, String PAYER_IDENTIFICATION_NUMBER, String PAYER_IDENTITY, String PAYER_FIRST_NAME, String PAYER_SURNAME, String PAYER_LANGUAGE_CODE, String PAYER_EMAIL, String PAYER_MSISDN, String PAYER_GENDER) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse(CONTENT_TYPE);
        JSONObject requestBodyJson = new JSONObject();
        JSONObject payeeJson = new JSONObject();
        payeeJson.put("partyId", PAYEE_PARTY_ID);
        payeeJson.put("partyIdType", PAYEE_PARTY_ID_TYPE);
        requestBodyJson.put("amount", AMOUNT);
        requestBodyJson.put("currency", CURRENCY);
        requestBodyJson.put("payee", payeeJson);
        requestBodyJson.put("externalId", EXTERNAL_ID);
        requestBodyJson.put("orginatingCountry", ORIGINATING_COUNTRY);
        requestBodyJson.put("originalAmount", ORIGINAL_AMOUNT);
        requestBodyJson.put("originalCurrency", ORIGINAL_CURRENCY);
        requestBodyJson.put("payerMessage", PAYER_MESSAGE);
        requestBodyJson.put("payeeNote", PAYEE_NOTE);
        requestBodyJson.put("payerIdentificationType", PAYER_IDENTIFICATION_TYPE);
        requestBodyJson.put("payerIdentificationNumber", PAYER_IDENTIFICATION_NUMBER);
        requestBodyJson.put("payerIdentity", PAYER_IDENTITY);
        requestBodyJson.put("payerFirstName", PAYER_FIRST_NAME);
        requestBodyJson.put("payerSurName", PAYER_SURNAME);
        requestBodyJson.put("payerLanguageCode", PAYER_LANGUAGE_CODE);
        requestBodyJson.put("payerEmail", PAYER_EMAIL);
        requestBodyJson.put("payerMsisdn", PAYER_MSISDN);
        requestBodyJson.put("payerGender", PAYER_GENDER);

        RequestBody body = RequestBody.create(mediaType, requestBodyJson.toString());
        Request request = new Request.Builder()
                .url("https://sandbox.momodeveloper.mtn.com/remittance/v2_0/cashtransfer")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + USER_TOKEN)
                .addHeader("X-Reference-Id", UUID)
                .addHeader("X-Callback-Url", "")
                .addHeader("X-Target-Environment", TARGET_ENVIRONMENT)
                .build();

        Response response = client.newCall(request).execute();

        // Get returned status code
        String status2 = String.valueOf(response.code());
        System.out.println("Cash Transfer Response");
        System.out.println("------------------------------");
        System.out.println("Response Code = " + status2);
        System.out.println("------------------------------");

        if (response.code() == 409) {
            JSONObject object = new JSONObject(response.body().string());

            System.out.println("Code = " + object.getString("code"));
            System.out.println("Message = " + object.getString("message"));
        }
        return response.code();
    }
}