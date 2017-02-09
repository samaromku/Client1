package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andrey.client1.R;
import com.example.andrey.client1.entities.Client;
import com.example.andrey.client1.entities.Request;
import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.storage.JsonParser;

public class TaskActivity extends AppCompatActivity{
    TaskManager manager;
    TextView taskTitle;
    TextView adress;
    TextView telephone;
    TextView taskBody;
    TextView deadLine;
    Button doneBtn;
    int taskNumber;
    EditText comment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);
        Intent intent = getIntent();
        manager = new TaskManager();
        taskNumber = intent.getIntExtra("taskNumber", 0);
        getSupportActionBar().setTitle(manager.getTasks().get(taskNumber).getTitle());

        initiate();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.getTasks().get(taskNumber).setCommentFromUser(comment.getText().toString());
                Client.INSTANCE.sendMessage(new JsonParser().requestAuth(new Request(manager.getTasks().get(taskNumber), "doneTask")));
                comment.setText("");
            }
        });
    }

    private void initiate(){
        taskTitle = (TextView) findViewById(R.id.task_title);
        adress = (TextView) findViewById(R.id.adress);
        telephone = (TextView) findViewById(R.id.telefon);
        taskBody = (TextView) findViewById(R.id.task_body);
        deadLine = (TextView) findViewById(R.id.deadline);
        doneBtn = (Button) findViewById(R.id.done);

        taskTitle.setText(manager.getTasks().get(taskNumber).getTitle());
        adress.setText(manager.getTasks().get(taskNumber).getAdress());
        telephone.setText(manager.getTasks().get(taskNumber).getTelephone());
        taskBody.setText(manager.getTasks().get(taskNumber).getBody());
        deadLine.setText(manager.getTasks().get(taskNumber).getDoneTime());
        comment = (EditText) findViewById(R.id.commentFromUser);
    }
}
