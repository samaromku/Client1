package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.andrey.client1.entities.Client;
import com.example.andrey.client1.entities.Request;
import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.storage.JsonParser;
import com.example.andrey.client1.R;

public class AuthActivity extends AppCompatActivity {
    Button signIn;
    EditText writeName;
    EditText writePwd;
    JsonParser parser = new JsonParser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);
        getSupportActionBar().setTitle("Авторизация");
        signIn = (Button) findViewById(R.id.sign_in);
        writeName = (EditText) findViewById(R.id.write_name);
        writePwd = (EditText) findViewById(R.id.write_pwd);
        System.out.println("пытаемся запустить клиент");

            Client.INSTANCE.start();
            System.out.println("клиент запущен");
        if(!Client.INSTANCE.isConnected()){
            Client.INSTANCE.setConnected(true);
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client.INSTANCE.sendMessage(parser.requestToServer(new Request(new User(writeName.getText().toString(), writePwd.getText().toString()), "auth")));
                writeName.setText("");
                writePwd.setText("");
                startActivity(new Intent(AuthActivity.this, AccountActivity.class).putExtra("fromAuth", "fromAuth"));
            }
        });
    }
}
