package com.aurorav2.digital.signage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    private ArrayList<String> schedules;

    @Override
    public void onReceive(Context context, Intent intent) {
        String weekDay = "";
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "MONDAY";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "TUESDAY";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "WEDNESDAY";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "THURSDAY";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "FRIDAY";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "SATURDAY";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "SUNDAY";
        }
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int ampm = c.get(Calendar.AM_PM);
        int minute = c.get(Calendar.MINUTE);

        boolean result = checkScheduleStartUp(context);
        if (result){
            Log.e("=======",weekDay + " - " + hour + " - " + ampm + " - " + minute);

            Log.e(">>>>>>>>", schedules.get(0));

            for (int i = 0; i < schedules.size(); i++){
                String record = schedules.get(i);
                String[] pattern1 = record.split("-");
                String dayOfTheWeek = pattern1[0].trim();
                if (!dayOfTheWeek.equals(weekDay))
                    continue;
                String[] pattern2 = pattern1[1].split("~");
                String[] pattern3 = pattern2[0].split(":");
                String currentHour = pattern3[0].trim();
                String currentMinute = pattern3[1].trim();
                String currentStatus = pattern3[2].trim();

                if (currentStatus.equals("PM")){
                    currentHour = String.valueOf(Integer.parseInt(currentHour) + 12);
                }

                if (!currentHour.equals(String.valueOf(hour)))
                    continue;
                if (!currentMinute.equals(String.valueOf(minute)))
                    continue;

                Log.e("+++", dayOfTheWeek + ", " + currentHour + ", " + currentMinute + ", " + currentStatus);

                context.startActivity(new Intent(context, AutoRunActivity.class));
            }
        }
    }

    private boolean checkScheduleStartUp(Context context) {
        String schedulePath = context.getFilesDir() + "/Schedule/";
        File dir = new File(schedulePath);
        if (!dir.exists()){
            return false;
        }
        File file = new File(schedulePath, "ScheduleList.txt");
        if (!file.exists() || !file.isFile()){
            return false;
        }
        ArrayList<String > list = getScheduleList(file);
        if (list == null || list.size() <= 0){
            return false;
        }
        getScheduleInformation(list);
        return true;
    }

    private ArrayList<String> getScheduleList(File file) {

        ArrayList<String> list = new ArrayList<>();
        try {
            FileInputStream stream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            try {
                String line = reader.readLine();
                list.add(line);
                while (line != null){
                    line = reader.readLine();
                    list.add(line);
                }
                return list;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getScheduleInformation(ArrayList<String> list) {
        schedules = new ArrayList<>();
        for (int i = 0; i < list.size() - 1; i++){
            String data = list.get(i).trim();
            String[] pattern = data.split("-");
            if (pattern.length < 2)
                continue;
            schedules.add(list.get(i));
        }
    }
}
