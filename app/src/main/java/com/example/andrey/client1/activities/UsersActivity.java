package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.andrey.client1.R;

public class UsersActivity extends AppCompatActivity{
    FloatingActionButton addUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_activity);
        getSupportActionBar().setTitle("Все пользователи");
        init();
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsersActivity.this, CreateUserActivity.class));
            }
        });
    }

    private void init(){
        addUser = (FloatingActionButton) findViewById(R.id.add_user);
    }
}
