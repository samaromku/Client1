package com.example.andrey.client1.test;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class SocketData
{
    Socket sock;
    Context ctx;
}

class GetPacket extends AsyncTask<SocketData, Integer, Integer>
{
    Context mCtx;
    char[] mData;
    Socket mySock;

    protected void onProgressUpdate(Integer... progress)
    {
        try
        {
// Получаем принятое от сервера сообщение
            String prop = String.valueOf(mData);
// Делаем с сообщением, что хотим. Я, например, пишу в базу

        }
        catch(Exception e)
        {
            Toast.makeText(mCtx, "Socket error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected void onPostExecute(Integer result)
    {
// Это выполнится после завершения работы потока
    }

    protected Integer doInBackground(SocketData... param)
    {
        mySock = param[0].sock;
        mCtx = param[0].ctx;
        mData = new char[4096];

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mySock.getInputStream()));
            int read = 0;

// Принимаем сообщение от сервера
// Данный цикл будет работать, пока соединение не оборвется
// или внешний поток не скажет данному cancel()
            while ((read = reader.read(mData)) >= 0 && !isCancelled())
            {
// "Вызываем" onProgressUpdate каждый раз, когда принято сообщение
                if(read > 0) publishProgress(read);
            }
            reader.close();
        } catch (IOException e) {
            return -1;
        }
        catch (Exception e) {
            return -1;
        }
        return 0;
    }
}
