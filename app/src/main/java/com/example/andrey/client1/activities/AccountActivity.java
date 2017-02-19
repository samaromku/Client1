package com.example.andrey.client1.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.andrey.client1.entities.Client;
import com.example.andrey.client1.entities.Request;
import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.storage.JsonParser;
import com.example.andrey.client1.storage.OnListItemClickListener;
import com.example.andrey.client1.R;
import com.example.andrey.client1.adapter.TasksAdapter;

import java.io.IOException;

public class AccountActivity extends AppCompatActivity {
    FloatingActionButton addTask;
    private TasksAdapter adapter;
    private RecyclerView tasksList;
    private JsonParser parser = new JsonParser();

    private OnListItemClickListener clickListener = (v, position) -> {
        Client.INSTANCE.sendMessage(parser.requestToServer(new Request(Client.INSTANCE.getTaskList().get(position), Request.WANT_SOME_COMMENTS)));
        startActivity(new Intent(AccountActivity.this, TaskActivity.class).putExtra("taskNumber", position));
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        checkFromAuth();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        getSupportActionBar().setTitle("Account");
        tasksList = (RecyclerView) findViewById(R.id.tasks_list);
        addTask = (FloatingActionButton) findViewById(R.id.add_task_btn);
        if(Client.INSTANCE.getRole()!=null && Client.INSTANCE.getRole().equals(User.ADMIN_ROLE)){
            addTask.setVisibility(View.VISIBLE);
        }else{
            addTask.setVisibility(View.GONE);
        }
        if(Client.INSTANCE.getRole()!=null) {
            System.out.println(Client.INSTANCE.getRole());
        }else
            System.out.println("role = " + Client.INSTANCE.getRole());

        addTask.setOnClickListener(v -> startActivity(new Intent(AccountActivity.this, CreateTaskActivity.class)));

        tasksList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TasksAdapter(Client.INSTANCE.getTaskList(), clickListener);
        adapter.notifyDataSetChanged();
        tasksList.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(Client.INSTANCE.getRole()!=null && Client.INSTANCE.getRole().equals(User.ADMIN_ROLE)){
            getMenuInflater().inflate(R.menu.admin_menu, menu);
        } else
            getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                try {
                    Client.INSTANCE.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(AccountActivity.this, AuthActivity.class));
                return true;

            case R.id.refresh:
                adapter.notifyDataSetChanged();
                return true;


            case R.id.users:
                startActivity(new Intent(AccountActivity.this, UsersActivity.class));
            return true;

            default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void checkFromAuth(){
        Intent intent = getIntent();
        Client.INSTANCE.setIntFromAuth(intent.getStringExtra("fromAuth"));
    }
}
