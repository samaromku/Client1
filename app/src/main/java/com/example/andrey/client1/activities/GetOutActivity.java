package com.example.andrey.client1.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class GetOutActivity extends AppCompatActivity{
    public void start() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }
}
