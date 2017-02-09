package com.example.andrey.client1.entities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    public static final Client INSTANCE = new Client();
    private static final String hostName = "192.168.0.186";
    private static final int portNumber = 60123;
    private static final String debugString = "дебаг";
    private Socket socket = null;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean isConnected = true;

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void start() {
        new Thread() {
            public void run() {
                try {
                        Log.i(debugString, "пытаемся подключиться к серверу");
                        socket = new Socket(hostName, portNumber);
                        Log.i(debugString, "удалось подключиться к серверу");
                    while(isConnected) {
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String data;
                        while ((data = reader.readLine()) != null) {
                            System.out.println("Сообщение от сервера: " + data);
                        }
                    }
                } catch (IOException e) {
                    Log.e(debugString, e.getMessage());
                }
            }
        }.start();
    }

    public void stop() throws IOException {
                writer.close();
                reader.close();
                socket.close();
        isConnected = false;

    }

    public void sendMessage(final String message) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        if(socket!=null) {
                            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            writer.write(message);
                            writer.newLine();
                            writer.flush();
                            System.out.println("сообщение отправлено 1");
                        }
                    } catch (
                            IOException e
                            )
                    {
                        Log.e(debugString, e.getMessage());
                    }
                }
            }).start();
    }
}
