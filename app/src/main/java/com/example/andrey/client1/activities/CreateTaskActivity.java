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
import android.widget.Toast;

import com.example.andrey.client1.R;
import com.example.andrey.client1.entities.Client;
import com.example.andrey.client1.entities.Request;
import com.example.andrey.client1.entities.Task;
import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.storage.DateUtil;
import com.example.andrey.client1.storage.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity{
    EditText taskTitle;
    EditText taskBody;
    EditText doneTime;
    EditText taskAdress;
    EditText telephone;
    Button createTask;
    AppCompatSpinner spinner;
    String[] data;
    List<User> users = new ArrayList<>();
    String userName;
    DateUtil dateUtil = new DateUtil();
    JsonParser parser = new JsonParser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_activity);
        getSupportActionBar().setTitle("Создание задания");
        init();
        System.out.println(new DateUtil().currentDate());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        btnClick();
    }

    private void btnClick(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userName = users.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userName = users.get(0).getName();
            }
        });

        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client.INSTANCE.sendMessage(parser.requestToServer(new Request(new Task(Client.INSTANCE.getTaskList().size()+1,
                        taskTitle.getText().toString(),
                        taskBody.getText().toString(),
                        dateUtil.currentDate(),
                        false,
                        doneTime.getText().toString(),
                        userName,
                        taskAdress.getText().toString(),
                        telephone.getText().toString(),
                        null), Request.ADD_TASK_TO_SERVER)));
                taskTitle.setText("");
                taskBody.setText("");
                doneTime.setText("");
                taskAdress.setText("");
                telephone.setText("");
                startActivity(new Intent(CreateTaskActivity.this, AccountActivity.class));
            }
        });
    }

    private void init(){
        taskTitle = (EditText) findViewById(R.id.task_title);
        taskBody = (EditText) findViewById(R.id.task_body);
        doneTime = (EditText) findViewById(R.id.done_time);
        taskAdress = (EditText) findViewById(R.id.task_adress);
        telephone = (EditText) findViewById(R.id.telephone);
        createTask = (Button) findViewById(R.id.create_task_btn);
        spinner = (AppCompatSpinner) findViewById(R.id.spinner_users);
        users.addAll(Client.INSTANCE.getUsers());
        data = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            data[i] = users.get(i).getName();
        }
    }
}
