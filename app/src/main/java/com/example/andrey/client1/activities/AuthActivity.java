package com.example.andrey.client1.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andrey.client1.network.CheckNetwork;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.entities.User;
import com.example.andrey.client1.service.GpsService;
import com.example.andrey.client1.storage.ConverterMessages;
import com.example.andrey.client1.R;
import com.example.andrey.client1.storage.SHAHashing;
import com.google.firebase.iid.FirebaseInstanceId;

public class AuthActivity extends AppCompatActivity {
    private Button signIn;
    private EditText writeName;
    private EditText writePwd;
    private SHAHashing hashing = new SHAHashing();
    private CheckBox isInside;
    private UsersManager usersManager = UsersManager.INSTANCE;
    private CheckNetwork checkNetwork = CheckNetwork.instance;
    private Client client = Client.INSTANCE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);
        getSupportActionBar().setTitle("Авторизация");
        signIn = (Button) findViewById(R.id.sign_in);
        isInside = (CheckBox) findViewById(R.id.inside_ip_checkbox);
        writeName = (EditText) findViewById(R.id.write_name);
        writePwd = (EditText) findViewById(R.id.write_pwd);
        System.out.println(FirebaseInstanceId.getInstance().getToken());
        //если нажата галочка, ставим внутреннюю сеть, если нет, внешнюю
        isInside.setOnCheckedChangeListener((buttonView, isChecked) -> checkNetwork.setNetworkInsideOrOutside(isChecked));

        signIn.setOnClickListener(v -> {
            //создаем нового польщователя на основе полученных логина и пароля
            User user1 = new User(writeName.getText().toString(), hashing.hashPwd(writePwd.getText().toString()));
            //добавляем пользователя в синглетон, чтобы в дальнейшем с ним работать
            usersManager.setUser(user1);
            //отправляем запрос к серверу, ждем ответ
            Intent intent = new Intent(AuthActivity.this, AccountActivity.class).putExtra("fromAuth", true);
            new UpdateAuth(this, new Request(user1, Request.AUTH), intent).execute();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private class UpdateAuth extends AsyncTask<Void, Void, Void>{
        private ProgressDialog dialog;
        private Context context;
        private Request request;
        private Intent intent;
        private ConverterMessages converter = new ConverterMessages();

        public UpdateAuth(Context context, Request request, Intent intent){
            this.context = context;
            this.dialog = new ProgressDialog(context);
            this.request = request;
            this.intent = intent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            converter.authMessage(request);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //стартуем сервис
            //если авторизация выполнена, выполняем нужные действия
            if(client.isAuth()) {
                startService(GpsService.newIntent(context));
                GpsService.setServiceAlarm(context, true);
                context.startActivity(intent);
            }
            //иначе показываем тост, что авторизация не выполнена.
            else{
                Toast.makeText(context, "Вы не авторизированы", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }
    }

}

