package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andrey.client1.R;
import com.example.andrey.client1.adapter.CommentsAdapter;
import com.example.andrey.client1.entities.Client;
import com.example.andrey.client1.entities.Comment;
import com.example.andrey.client1.entities.Request;
import com.example.andrey.client1.entities.Task;
import com.example.andrey.client1.storage.DateUtil;
import com.example.andrey.client1.storage.JsonParser;
import com.example.andrey.client1.storage.OnListItemClickListener;

public class TaskActivity extends AppCompatActivity{
    private TextView typeTask;
    private TextView importance;
    private TextView orgName;
    private TextView address;
    private TextView telephone;
    private TextView taskBody;
    private TextView deadLine;
    private Button doneBtn;
    private int taskNumber;
    private EditText comment;
    private Button needHelp;
    private Button disAgree;
    private RecyclerView commentsList;
    private CommentsAdapter adapter;

    private OnListItemClickListener clickListener = (v, position) -> {};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);
        Intent intent = getIntent();
        taskNumber = intent.getIntExtra("taskNumber", 0);
        getSupportActionBar().setTitle(Client.INSTANCE.getTaskList().get(taskNumber).getStatus());
        initiate();
        commentsList.setLayoutManager(new LinearLayoutManager(this));
        System.out.println(Client.INSTANCE.getComments().size());
        adapter = new CommentsAdapter(Client.INSTANCE.getComments(), clickListener);
        adapter.notifyDataSetChanged();
        commentsList.swapAdapter(adapter, true);
        btnClicks();
    }

    private void btnClicks(){
        doneBtn.setOnClickListener(v -> clickOnButton(Task.DONE_TASK));
        needHelp.setOnClickListener(v -> clickOnButton(Task.NEED_HELP));
        disAgree.setOnClickListener(v -> clickOnButton(Task.DISAGREE_TASK));
    }

    private void clickOnButton(String actionTask){
        if(comment.getText().toString().equals("")){
            comment.setHint("Вы должны заполнить это поле");
        }
        else {
            Client.INSTANCE.getTaskList().get(taskNumber).setComment(new Comment(new DateUtil().currentDate(), comment.getText().toString()));
            Client.INSTANCE.sendMessage(new JsonParser().requestToServer(new Request(Client.INSTANCE.getTaskList().get(taskNumber), actionTask)));
            comment.setText("");
            Intent i = new Intent(TaskActivity.this, AccountActivity.class);
            i.putExtra("taskNumber", taskNumber);
            startActivity(i);
        }
    }

    private void initiate(){
        commentsList = (RecyclerView) findViewById(R.id.comments);
        typeTask = (TextView) findViewById(R.id.type_task);
        importance = (TextView) findViewById(R.id.importance);
        orgName = (TextView) findViewById(R.id.org_name);
        address = (TextView) findViewById(R.id.address);
        telephone = (TextView) findViewById(R.id.telefon);
        taskBody = (TextView) findViewById(R.id.task_body);
        deadLine = (TextView) findViewById(R.id.deadline);
        doneBtn = (Button) findViewById(R.id.done);
        needHelp = (Button) findViewById(R.id.need_help);
        disAgree = (Button) findViewById(R.id.disagree);

        typeTask.setText(Client.INSTANCE.getTaskList().get(taskNumber).getType());
        importance.setText(Client.INSTANCE.getTaskList().get(taskNumber).getImportance());
        orgName.setText(Client.INSTANCE.getTaskList().get(taskNumber).getOrgName());
        address.setText(Client.INSTANCE.getTaskList().get(taskNumber).getAddress());
        //telephone.setText(Client.INSTANCE.getTaskList().get(taskNumber).getTelephone());
        taskBody.setText(Client.INSTANCE.getTaskList().get(taskNumber).getBody());
        deadLine.setText(Client.INSTANCE.getTaskList().get(taskNumber).getDoneTime());
        comment = (EditText) findViewById(R.id.commentFromUser);
    }
}
