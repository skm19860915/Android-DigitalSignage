package com.aurorav2.digital.signage;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.prefs.PreferenceChangeEvent;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_AND_WRITE_SDK = 1;
    private ArrayList<String> schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkStoragePermission();

        boolean isAutoStartUp = checkAutoStartUp();
        //boolean isScheduleStartUp = checkScheduleStartUp();

        runAlarmProcess();

        if (!isAutoStartUp){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void runAlarmProcess() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1*60*1000, pendingIntent);
        ComponentName componentName = new ComponentName(MainActivity.this, AlarmReceiver.class);
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

//    private boolean checkScheduleStartUp() {
//        String schedulePath = getFilesDir() + "/Schedule/";
//        File dir = new File(schedulePath);
//        if (!dir.exists()){
//            return false;
//        }
//        File file = new File(schedulePath, "ScheduleList.txt");
//        if (file.isFile() && file.exists()){
//            ArrayList<String > list = getScheduleList(file);
//            if (list == null || list.size() <= 0){
//                return false;
//            }
//            getScheduleInformation(list);
//            return true;
//        }
//        return false;
//    }
//
//    private void getScheduleInformation(ArrayList<String> list) {
//        schedules = new ArrayList<>();
//        for (int i = 0; i < list.size() - 1; i++){
//            String data = list.get(i).trim();
//            String[] pattern1 = data.split("-");
//            if (pattern1.length < 2)
//                continue;
//            schedules.add(list.get(i));
//        }
//    }
//
//    private ArrayList<String> getScheduleList(File file) {
//
//        ArrayList<String> list = new ArrayList<>();
//        try {
//            FileInputStream stream = new FileInputStream(file);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//            try {
//                String line = reader.readLine();
//                list.add(line);
//                while (line != null){
//                    line = reader.readLine();
//                    list.add(line);
//                }
//                return list;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private boolean checkAutoStartUp() {
        String schedulePath = getFilesDir() + "/Schedule/";
        File dir = new File(schedulePath);
        if (!dir.exists()){
            return false;
        }
        File file = new File(schedulePath, "AutoStartUp.txt");
        if (file.isFile() && file.exists()){
            getProjectList();
            return true;
        }
        return false;
    }

    private void getProjectList() {
        String configPath = getFilesDir() + "/Configs/";
        File dir = new File(configPath);
        if (!dir.exists())
            return;
        final File[] files = dir.listFiles();
        if (files == null || files.length <= 0)
            return;
        runActivity();
    }

    private void runActivity() {
        Intent intent = new Intent(MainActivity.this, AutoRunActivity.class);
        startActivity(intent);
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_AND_WRITE_SDK);
            return;
        }
    }
}
