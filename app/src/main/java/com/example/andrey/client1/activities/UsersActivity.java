package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.andrey.client1.R;
import com.example.andrey.client1.adapter.UserAdapter;
import com.example.andrey.client1.entities.UserRole;
import com.example.andrey.client1.managers.UserRolesManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.storage.JsonParser;
import com.example.andrey.client1.storage.OnListItemClickListener;
import com.example.andrey.client1.storage.UpdateData;

public class UsersActivity extends AppCompatActivity{
    FloatingActionButton addUser;
    RecyclerView userList;
    UserAdapter adapter;
    UsersManager usersManager = UsersManager.INSTANCE;
    UserRolesManager userRolesManager = UserRolesManager.INSTANCE;
    Client client = Client.INSTANCE;
    JsonParser parser = new JsonParser();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_activity);
        getSupportActionBar().setTitle("Пользователи");
        init();
        adminAction();

        userList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(usersManager.getUsers(), clickListener);
        userList.setAdapter(adapter);
        update();
    }

    private OnListItemClickListener clickListener = (v, position) -> {
        startActivity(new Intent(UsersActivity.this, UserActivity.class).putExtra("position", position));
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UsersActivity.this, AccountActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
            client.sendMessage(parser.requestToServer(new Request(Request.GIVE_ME_LAST_USERS_COORDS)));
            startActivity(new Intent(this, MapActivity.class));
            return true;

            default:
            return super.onOptionsItemSelected(item);
        }
    }


    private void update(){
        Intent intent = getIntent();
        boolean isNewUser = intent.getBooleanExtra("newUser", false);
        boolean isRemoveUser = intent.getBooleanExtra("removeUser", false);
        if(isNewUser || isRemoveUser){
            UpdateData data = new UpdateData(this, adapter);
            data.execute();
        }
    }

    private void adminAction(){
        UserRole userRole = userRolesManager.getUserRole();
        //настройка видимости кнопки
        if(userRole!=null && userRole.isMakeNewUser()){
        addUser.setVisibility(View.VISIBLE);
        }else addUser.setVisibility(View.INVISIBLE);
        addUser.setOnClickListener(v -> startActivity(new Intent(UsersActivity.this, CreateUserActivity.class)));
    }

    private void init(){
        addUser = (FloatingActionButton) findViewById(R.id.add_user);
        userList = (RecyclerView) findViewById(R.id.users);
    }
}
