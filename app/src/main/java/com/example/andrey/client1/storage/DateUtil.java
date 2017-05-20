package com.example.andrey.client1.storage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
    private SimpleDateFormat forServer = new SimpleDateFormat("yy-MM-dd HH:mm");
    private DateFormat fromClient = new SimpleDateFormat("dd-MM-yy HH:mm");

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

    public String getDDFromYY(String date){
        try {
            Date date1 = forServer.parse(date);
            return fromClient.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

//    public String  convertDate(String date){
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//        try {
//            Date date1 = format.parse(date);
//            SimpleDateFormat rightFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
//            return rightFormat.format(date1);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return date;
//        }
//    }
}
