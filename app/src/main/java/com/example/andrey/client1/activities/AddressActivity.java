package com.example.andrey.client1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.andrey.client1.R;
import com.example.andrey.client1.storage.UpdateData;
import com.example.andrey.client1.adapter.AddressAdapter;
import com.example.andrey.client1.managers.AddressManager;
import com.example.andrey.client1.storage.OnListItemClickListener;

public class AddressActivity extends AppCompatActivity {
    RecyclerView addressesList;
    AddressAdapter adapter;
    private AddressManager addressManager = AddressManager.INSTANCE;
    public static final String TAG = "AddressActivity";

    private OnListItemClickListener clickListener = (v, position) -> {};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_activity);
        getSupportActionBar().setTitle("Адреса");
        init();
        addressesList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressAdapter(addressManager.getAddresses(), clickListener);
        addressesList.setAdapter(adapter);
        UpdateData test = new UpdateData(this, adapter);
        test.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, CreateTaskActivity.class));
    }

    private void init(){
        addressesList = (RecyclerView) findViewById(R.id.addresses);
    }
}
