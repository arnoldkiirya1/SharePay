package com.sharepay.ug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class GroupChannel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_channel);
    }

    public void makePayment(){
        Intent intent = new Intent(this, Deposit.class);
        startActivity(intent);
    }
}