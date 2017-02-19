package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.andrey.client1.R;
import com.example.andrey.client1.adapter.UserAdapter;
import com.example.andrey.client1.entities.Client;
import com.example.andrey.client1.entities.Request;
import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.storage.OnListItemClickListener;

public class UsersActivity extends AppCompatActivity{
    FloatingActionButton addUser;
    RecyclerView userList;
    UserAdapter adapter;

    private OnListItemClickListener clickListener = (v, position) -> {
        startActivity(new Intent(UsersActivity.this, UserActivity.class));
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_activity);
        getSupportActionBar().setTitle("Users");
        init();
        adminAction();

        userList.setLayoutManager(new LinearLayoutManager(this));
        System.out.println(Client.INSTANCE.getUsers().size());
        adapter = new UserAdapter(Client.INSTANCE.getUsers(), clickListener);
        adapter.notifyDataSetChanged();
        userList.setAdapter(adapter);
    }

    private void adminAction(){
        if(Client.INSTANCE.getRole()!=null && Client.INSTANCE.getRole().equals(User.ADMIN_ROLE)){
            addUser.setVisibility(View.VISIBLE);
        }else{
            addUser.setVisibility(View.GONE);
        }
        addUser.setOnClickListener(v -> startActivity(new Intent(UsersActivity.this, CreateUserActivity.class)));
    }

    private void init(){
        addUser = (FloatingActionButton) findViewById(R.id.add_user);
        userList = (RecyclerView) findViewById(R.id.users);
    }
}
