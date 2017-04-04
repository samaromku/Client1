package com.example.andrey.client1.storage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
    private SimpleDateFormat forServer = new SimpleDateFormat("yy-MM-dd HH:mm");

    private Date date = new Date();

    public Date getDate() {
        return date;
    }

    public String dateForServer(Date date){
        return forServer.format(date);
    }

    public String currentDateForServer(){
        return forServer.format(date);
    }

    public String currentDate(){
        return dateFormat.format(date);
    }
}
