package com.aurorav2.digital.signage.Component;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.aurorav2.digital.signage.R;
import com.aurorav2.digital.signage.ScheduleDialog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ScheduleFragment extends Fragment implements ScheduleDialog.OnCompleteListener {
    private CheckBox autoPlayCheckBox;
    private CheckBox saveScheduleCheckBox;
    private Button setupButton;
    private ListView scheduleListView;
    private ScheduleDialog scheduleDialog;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        autoPlayCheckBox = rootView.findViewById(R.id.autoPlayCheckBox);
        saveScheduleCheckBox = rootView.findViewById(R.id.saveScheduleCheckBox);
        setupButton = rootView.findViewById(R.id.setupButton);
        scheduleListView = rootView.findViewById(R.id.scheduleListView);
        initializeScheduleList();

        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleDialog = new ScheduleDialog();
                scheduleDialog.setTargetFragment(ScheduleFragment.this, 0);
                scheduleDialog.show(getFragmentManager(), "");
            }
        });

        autoPlayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveAutoConfigs(isChecked);
            }
        });

        saveScheduleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveScheduleConfigs(isChecked);
            }
        });

        return rootView;
    }

    private void initializeScheduleList() {
        arrayList = new ArrayList<>();
        arrayList.add("SUNDAY");
        arrayList.add("MONDAY");
        arrayList.add("TUESDAY");
        arrayList.add("WEDNESDAY");
        arrayList.add("THURSDAY");
        arrayList.add("FRIDAY");
        arrayList.add("SATURDAY");
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        scheduleListView.setAdapter(adapter);
    }

    private void saveScheduleConfigs(boolean isChecked) {
        String schedulePath = getContext().getFilesDir() + "/Schedule/";
        File dir = new File(schedulePath);

        if (isChecked){
            if (!dir.exists()){
                dir.mkdirs();
            }
            File file = new File(schedulePath + "ScheduleList.txt");
            if (file.exists() && file.isFile())
                file.delete();
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                String firstRecord = arrayList.get(0);
                bufferedWriter.write(firstRecord + "\n");
                for (int i = 1; i < arrayList.size(); i++){
                    String record = arrayList.get(i);
                    StringBuilder builder = new StringBuilder(record);
                    bufferedWriter.write(builder.toString() + "\n");
                }
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toasty.error(getContext(), "Failed to Save File!", Toast.LENGTH_SHORT, true).show();
            }
        }
        else{
            if (!dir.exists()){
                return;
            }
            File file = new File(schedulePath + "ScheduleList.txt");
            if (file.exists() && file.isFile()){
                file.delete();
            }
        }
    }

    private void saveAutoConfigs(boolean isChecked) {
        String schedulePath = getContext().getFilesDir() + "/Schedule/";
        File dir = new File(schedulePath);
        if (isChecked){
            if (!dir.exists()){
                dir.mkdirs();
            }
            File file = new File(schedulePath + "AutoStartUp.txt");
            if (file.exists() && file.isFile())
                return;
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write("true");
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toasty.error(getActivity(), "Failed to Save File!", Toast.LENGTH_SHORT, true).show();
            }
        }
        else{
            if (!dir.exists()){
                return;
            }
            File file = new File(schedulePath + "AutoStartUp.txt");
            if (file.exists() && file.isFile()){
                file.delete();
            }
        }
    }

    @Override
    public void onComplete(String value) {
        String[] parameters = value.split(",");
        int dayOfWeekIndex = Integer.parseInt(parameters[0]);
        for (int i = 0; i < arrayList.size(); i++){
            if (i == dayOfWeekIndex){
                arrayList.set(i, parameters[1]);
                break;
            }
        }
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        scheduleListView.setAdapter(adapter);
    }
}

