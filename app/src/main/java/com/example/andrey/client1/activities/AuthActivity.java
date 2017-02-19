package com.example.andrey.client1.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.andrey.client1.storage.SHAHashing;

import java.security.NoSuchAlgorithmException;

public class AuthActivity extends AppCompatActivity {
    Button signIn;
    EditText writeName;
    EditText writePwd;
    JsonParser parser = new JsonParser();
    SHAHashing hashing = new SHAHashing();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);
        getSupportActionBar().setTitle("Auth");
        signIn = (Button) findViewById(R.id.sign_in);
        writeName = (EditText) findViewById(R.id.write_name);
        writePwd = (EditText) findViewById(R.id.write_pwd);
            Client.INSTANCE.start();
        if(!Client.INSTANCE.isConnected()){
            Client.INSTANCE.setConnected(true);
        }

        signIn.setOnClickListener(v -> {
            try {
                Client.INSTANCE.sendMessage(parser.requestToServer(new Request(new User(writeName.getText().toString(), hashing.hashPwd(writePwd.getText().toString())), "auth")));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            writeName.setText("");
            writePwd.setText("");
            startActivity(new Intent(AuthActivity.this, AccountActivity.class).putExtra("fromAuth", "fromAuth"));
        });
    }
}
