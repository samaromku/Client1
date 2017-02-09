package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.andrey.client1.entities.Client;
import com.example.andrey.client1.storage.OnListItemClickListener;
import com.example.andrey.client1.R;
import com.example.andrey.client1.entities.Task;
import com.example.andrey.client1.adapter.TasksAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    private Button logout;
    private TasksAdapter adapter;
    RecyclerView tasksList;
    private List<Task> tasks = new ArrayList<>();
    TaskManager manager = new TaskManager();


    private OnListItemClickListener clickListener = new OnListItemClickListener() {
        @Override
        public void onClick(View v, int position) {
                Intent intent = new Intent(AccountActivity.this, TaskActivity.class);
                intent.putExtra("taskNumber", position);
                startActivity(intent);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        getSupportActionBar().setTitle("Аккаунт");
        tasksList = (RecyclerView) findViewById(R.id.tasks_list);
        tasks.addAll(manager.getTasks());
        logout = (Button) findViewById(R.id.logOutBtn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Client.INSTANCE.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(AccountActivity.this, AuthActivity.class));
            }
        });
        tasksList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TasksAdapter(tasks, clickListener);
        tasksList.setAdapter(adapter);

    }
}
