package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        //if(!Client.INSTANCE.isConnected()) {
        //System.out.println(Client.INSTANCE.isConnected());
            Client.INSTANCE.start();
          //  Client.INSTANCE.setConnected(true);
            System.out.println("клиент запущен");
        //System.out.println(Client.INSTANCE.isConnected());
        //}


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Client.INSTANCE.sendMessage(parser.parseAuthUser(writeName.getText().toString(), writePwd.getText().toString()));
                Client.INSTANCE.sendMessage(parser.requestAuth(new Request(new User(writeName.getText().toString(), writePwd.getText().toString()), "auth")));
                writeName.setText("");
                writePwd.setText("");
                Intent intent = new Intent(AuthActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
