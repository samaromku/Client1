package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andrey.client1.R;
import com.example.andrey.client1.entities.Client;
import com.example.andrey.client1.entities.Request;
import com.example.andrey.client1.storage.JsonParser;

public class TaskActivity extends AppCompatActivity{
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
        taskNumber = intent.getIntExtra("taskNumber", 0);
        getSupportActionBar().setTitle(Client.INSTANCE.getTaskList().get(taskNumber).getTitle());

        initiate();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client.INSTANCE.getTaskList().get(taskNumber).setCommentFromUser(comment.getText().toString());
                Client.INSTANCE.sendMessage(new JsonParser().requestToServer(new Request(Client.INSTANCE.getTaskList().get(taskNumber), "doneTask")));
                comment.setText("");
                v.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                v.setClickable(false);
                Intent i = new Intent(TaskActivity.this, AccountActivity.class);
                i.putExtra("taskNumber", taskNumber);
                startActivity(i);
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

        taskTitle.setText(Client.INSTANCE.getTaskList().get(taskNumber).getTitle());
        adress.setText(Client.INSTANCE.getTaskList().get(taskNumber).getAdress());
        telephone.setText(Client.INSTANCE.getTaskList().get(taskNumber).getTelephone());
        taskBody.setText(Client.INSTANCE.getTaskList().get(taskNumber).getBody());
        deadLine.setText(Client.INSTANCE.getTaskList().get(taskNumber).getDoneTime());
        comment = (EditText) findViewById(R.id.commentFromUser);
    }
}
