package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrey.client1.R;
import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.storage.DataWorker;

public class UserActivity extends AppCompatActivity{
    private TextView login;
    private TextView fio;
    private TextView role;
    private TextView phone;
    private TextView email;
    private int position;
    private User user;
    private ImageView change;
    UsersManager usersManager = UsersManager.INSTANCE;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        init();
        getSupportActionBar().setTitle(user.getLogin());
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(this, "ты нажал на кнопку редактирования", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserActivity.this, UserRoleActivity.class).putExtra("userId", user.getId()));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserActivity.this, UsersActivity.class));
    }

    private void init(){
        change = (ImageView) findViewById(R.id.change);
        login = (TextView) findViewById(R.id.login);
        fio = (TextView) findViewById(R.id.fio);
        role = (TextView) findViewById(R.id.role);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);

        user = usersManager.getUsers().get(position);
        login.setText(user.getLogin());
        fio.setText(user.getFIO());
        role.setText(user.getRole());
        phone.setText(user.getTelephone());
        email.setText(user.getEmail());
    }
}
