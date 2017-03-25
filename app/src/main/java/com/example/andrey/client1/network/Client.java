package com.example.andrey.client1.network;

import android.util.Log;

import com.example.andrey.client1.storage.DataWorker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Client {
    public static final Client INSTANCE = new Client();
    //    private static final String hostNameHome = "10.42.112.233";  // ноут алины
    private static final String hostNameHome = "192.168.137.1";  // ноут алины
//    private static final String hostNameHome = "192.168.0.98";  // внутренний тепломер
    private static final String hostNameOutside = "81.23.123.230";  // тепломер
    private static final int portNumber = 60123;
    private static final String debugString = "debug";
    private static final String TAG = "Client";
    private Socket socket = null;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean isConnected = false;
    private String data;
    private InetAddress inetAddress;
    private SocketAddress sockAdd;
    private boolean isHomeIp = true;
    private volatile boolean isMessageReceived = false;
    private boolean auth = false;
    private volatile DataWorker thread;

    public DataWorker getThread() {
        return thread;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean isMessageReceived() {
        return isMessageReceived;
    }

    public void setMessageReceived(boolean messageReceived) {
        isMessageReceived = messageReceived;
    }

    public boolean isHomeIp() {
        return isHomeIp;
    }

    public void setHomeIp(boolean homeIp) {
        isHomeIp = homeIp;
    }

    private void connect(String hostName){
        try {
            inetAddress  = InetAddress.getByName(hostName);
            sockAdd = new InetSocketAddress(inetAddress, portNumber);
            socket = new Socket();

            } catch (IOException e) {
                System.out.println("не подключается 2");
                e.printStackTrace();
            }
        }

    private void tryConnectSocket(){
        if(isHomeIp){
            System.out.println(hostNameHome);
            connect(hostNameHome);
        }else {
            System.out.println(hostNameOutside);
            connect(hostNameOutside);
        }
    }

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
                    tryConnectSocket();
                    Log.i(debugString, "try connect server");
                    socket.connect(sockAdd);
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while(isConnected) {
                        while ((data = reader.readLine()) != null) {
                            if(!socket.isClosed()) {
//                                isMessageRecieved();
                                if(!data.equals("")) {
                                    System.out.println("Запускаем поток dataworker");
                                    thread = new DataWorker(data);
                                    System.out.println(data);
                                    thread.start();
                                    try {
                                        thread.join();
                                        thread = null;
                                        System.out.println("дождались и приравняли наллу");
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
//                                DataWorker.INSTANCE.workingWithData(data);
                            }
                            else {
                                while (socket.isClosed()) {
                                    System.out.println("пытаемся открыть закрытый сокет");
                                    socket.connect(sockAdd);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("не подключается 1");
                    Log.e(debugString, e.getMessage());
                }
            }
        }.start();
    }

    private void isMessageRecieved(){
        Log.i("Client", "message riceived" + data);
        setMessageReceived(true);
    }

    public void stop() throws IOException {
        if(socket!=null && reader!=null && writer!=null) {
            writer.close();
            reader.close();
            socket.close();
        }
        isConnected = false;
    }

    public void sendMessage(final String message) {
            new Thread(() -> {
                try
                {
                    if(socket!=null) {
                        if (socket.isConnected() && !socket.isClosed())
                        {
                                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                writer.write(message);
                                writer.newLine();
                                writer.flush();
                                System.out.println("message sent " + message);
                        }
                        else
                            socket.connect(sockAdd);
                    }
                } catch (
                        IOException e
                        )
                {
                    System.out.println("не удалось подключиться 4");
                    System.out.println(socket.isConnected());
                    Log.e(debugString, e.getMessage());
                }
            }).start();
    }
}
