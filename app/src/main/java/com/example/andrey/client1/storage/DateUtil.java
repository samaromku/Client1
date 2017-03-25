package com.example.andrey.client1.storage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
    Date date = new Date();

    public String currentDate(){
        return dateFormat.format(date);
    }
}
