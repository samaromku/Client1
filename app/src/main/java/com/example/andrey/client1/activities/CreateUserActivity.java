package com.example.andrey.client1.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.andrey.client1.R;

/**
 * Created by andrey on 14.02.2017.
 */

public class CreateUserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);
        getSupportActionBar().setTitle("Создать нового юзера");
    }
}
