package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.andrey.client1.R;
import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.entities.UserRole;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.storage.JsonParser;
import com.example.andrey.client1.storage.SHAHashing;

public class CreateUserActivity extends AppCompatActivity {
    EditText login;
    EditText password;
    EditText fio;
    AppCompatSpinner role;
    EditText phone;
    EditText email;
    Button create;
    String[] roles;
    private User user;
    String roleName;
    JsonParser parser = new JsonParser();
    boolean isChecked = false;
    SHAHashing hashing = new SHAHashing();
    private Client client = Client.INSTANCE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);
        getSupportActionBar().setTitle("Создать пользователя");
        init();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(adapter);
        role.setSelection(0);
        btnClick();
        create.setOnClickListener(v -> {
            createUser();
        });

    }


    private void btnClick() {
        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roleName = roles[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                roleName = roles[2];
            }
        });
    }


    private void createUser(){
        checkFieldsFull();

        if(isChecked) {
            user = new User(
                    login.getText().toString(),
                    hashing.hashPwd(password.getText().toString()),
                    fio.getText().toString(),
                    roleName,
                    phone.getText().toString(),
                    email.getText().toString()
            );
            client.sendMessage(parser.requestToServer(new Request(user, Request.ADD_NEW_USER)));
            login.setText("");
            password.setText("");
            phone.setText("");
            email.setText("");
            fio.setText("");
            startActivity(new Intent(CreateUserActivity.this, UsersActivity.class).putExtra("newUser", true));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CreateUserActivity.this, UsersActivity.class));
    }

    private void checkFieldsFull(){
        if(login.getText().toString().equals("")){
            login.setHint("");
            login.setHint("Вы должны заполнить это поле");
        }
        else if(password.getText().toString().equals("")){
            password.setText("");
            password.setHint("Вы должны заполнить это поле");
        }
        else if(fio.getText().toString().equals("")){
            fio.setText("");
            fio.setHint("Вы должны заполнить это поле");
        }
        else if(phone.getText().toString().equals("")){
            phone.setText("");
            phone.setHint("Вы должны заполнить это поле");
        }
        else if(email.getText().toString().equals("")){
            email.setText("");
            email.setHint("Вы должны заполнить это поле");
        }else
            isChecked=true;
    }

    private void init(){
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        fio = (EditText) findViewById(R.id.fio);
        create = (Button) findViewById(R.id.create);
        role = (AppCompatSpinner) findViewById(R.id.spinner_roles);
        roles  = new String[]{UserRole.USER_ROLE, UserRole.MANAGER_ROLE, UserRole.ADMIN_ROLE};
    }
}
