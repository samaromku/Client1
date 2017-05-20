package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrey.client1.R;
import com.example.andrey.client1.storage.Updater;
import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.managers.UserRolesManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.storage.ConverterMessages;

public class UserActivity extends AppCompatActivity{
    private User user;
    private ImageView change;
    UsersManager usersManager = UsersManager.INSTANCE;
    UserRolesManager userRolesManager = UserRolesManager.INSTANCE;
    private ConverterMessages converter = new ConverterMessages();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        init();
        getSupportActionBar().setTitle(user.getLogin());
        change.setOnClickListener(v -> {
            startActivity(new Intent(UserActivity.this, UserRoleActivity.class).putExtra("userId", user.getId()));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserActivity.this, UsersActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(userRolesManager.getUserRole().isMakeNewUser()) {
            getMenuInflater().inflate(R.menu.user_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_user:
                Intent intent = new Intent(this, UsersActivity.class).putExtra("removeUser", true);
                usersManager.setRemoveUser(user);
                new Updater(this, new Request(user, Request.REMOVE_USER), intent).execute();
//                converter.sendMessage(new Request(user, Request.REMOVE_USER));

//                startActivity(new Intent(this, UsersActivity.class).putExtra("removeUser", true));
                return true;

            default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void init(){
        change = (ImageView) findViewById(R.id.change);
        TextView login = (TextView) findViewById(R.id.login);
        TextView fio = (TextView) findViewById(R.id.fio);
        TextView role = (TextView) findViewById(R.id.role);
        TextView phone = (TextView) findViewById(R.id.phone);
        TextView email = (TextView) findViewById(R.id.email);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        user = usersManager.getUsers().get(position);

        login.setText(user.getLogin());
        fio.setText(user.getFIO());
        role.setText(user.getRole());
        phone.setText(user.getTelephone());
        email.setText(user.getEmail());
    }
}
