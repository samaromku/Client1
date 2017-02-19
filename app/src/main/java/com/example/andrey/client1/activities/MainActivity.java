package com.example.andrey.client1.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.andrey.client1.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String hostName = "192.168.0.186";
    private static final int portNumber = 60123;
    private static final String debugString = "дебаг";
    private Socket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button send = (Button) findViewById(R.id.send);
    }

    public void sendMessage(View view) {
                try
                {
                    EditText editText = (EditText) findViewById(R.id.write_text);
                    BufferedWriter writer  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(editText.getText().toString());
                    editText.setText("");
                    writer.newLine();
                    writer.flush();
                } catch (
                        IOException e
                        )
                {
                    Log.e(debugString, e.getMessage());
                }
    }
}
