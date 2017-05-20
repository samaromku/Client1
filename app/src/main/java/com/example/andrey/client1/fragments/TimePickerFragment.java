package com.example.andrey.client1.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.andrey.client1.R;
import com.example.andrey.client1.storage.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "extra_date";
    public static final String ARGS_DATE = "date";
    int year;
    int month;
    int day;
    DateUtil dateUtil = new DateUtil();

    private TimePicker timePicker;

    public static TimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_DATE, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Date date = (Date) getArguments().getSerializable(ARGS_DATE);
        System.out.println("получили дату в таймпикере" + date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int hours = calendar.get(Calendar.HOUR_OF_DAY);
        final int minutes = calendar.get(Calendar.MINUTE);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        timePicker = (TimePicker) view.findViewById(R.id.time_picker);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hours);
            timePicker.setMinute(minutes);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Выбери время")
                .setPositiveButton("Подтвердить", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hours=0;
                        int minutes=0;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hours = timePicker.getHour();
                            minutes = timePicker.getMinute();
                        }else{
                            hours = timePicker.getCurrentHour();
                            minutes = timePicker.getCurrentMinute();
                        }
                        Date date1 = new GregorianCalendar(year, month, day, hours, minutes).getTime();
                        Button activityChooseDate = (Button)getActivity().findViewById(R.id.choose_date);
                        activityChooseDate.setText(dateUtil.getDDFromYY(dateUtil.dateForServer(date1)));
                    }
                })
                .create();
    }
}

