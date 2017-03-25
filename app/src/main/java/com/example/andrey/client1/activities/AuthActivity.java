package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.storage.DataWorker;
import com.example.andrey.client1.network.Request;
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
    private CheckBox isInside;
    private DataWorker dataWorker = new DataWorker();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);
        getSupportActionBar().setTitle("Авторизация");
        signIn = (Button) findViewById(R.id.sign_in);
        isInside = (CheckBox) findViewById(R.id.inside_ip_checkbox);
        writeName = (EditText) findViewById(R.id.write_name);
        writePwd = (EditText) findViewById(R.id.write_pwd);


        isInside.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Client.INSTANCE.setHomeIp(isChecked);
            }
        });

        signIn.setOnClickListener(v -> {
            Client.INSTANCE.start();
            Client.INSTANCE.setHomeIp(isInside.isChecked());
            if(!Client.INSTANCE.isConnected()){
                Client.INSTANCE.setConnected(true);
            }
            dataWorker.setUserName(writeName.getText().toString());
//            DataWorker.INSTANCE.setUserName(writeName.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Client.INSTANCE.sendMessage(parser.requestToServer(new Request(new User(writeName.getText().toString(), hashing.hashPwd(writePwd.getText().toString())), "auth")));
                }
            }).start();
            writeName.setInputType(InputType.TYPE_NULL);
            writePwd.setInputType(InputType.TYPE_NULL);
            startActivity(new Intent(AuthActivity.this, AccountActivity.class).putExtra("fromAuth", true));
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}
