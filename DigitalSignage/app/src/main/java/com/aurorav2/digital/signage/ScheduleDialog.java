package com.aurorav2.digital.signage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

public class ScheduleDialog extends DialogFragment {
    private Spinner weekSpinner;
    private String dayOfWeek[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private ArrayAdapter<String> weekAdapter;
    private TimePicker fromTimePicker;
    private TimePicker toTimePicker;
    private Button okButton;
    private Button cancelButton;
    private String dayOfWeekName;
    private String dayOfWeekIndex;
    private String fromTime;
    private String toTime;

    private OnCompleteListener listener;

    public interface OnCompleteListener{
        void onComplete(String value);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_schedule, container, false);
        weekSpinner = view.findViewById(R.id.weekSpinner);
        weekAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dayOfWeek);
        weekSpinner.setAdapter(weekAdapter);
        fromTimePicker = view.findViewById(R.id.fromTimePicker);
        toTimePicker = view.findViewById(R.id.toTimePicker);
        okButton = view.findViewById(R.id.okButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        weekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayOfWeekIndex = String.valueOf(position);
                switch (position){
                    case 0:
                        dayOfWeekName = "SUNDAY";
                        break;
                    case 1:
                        dayOfWeekName = "MONDAY";
                        break;
                    case 2:
                        dayOfWeekName = "TUESDAY";
                        break;
                    case 3:
                        dayOfWeekName = "WEDNESDAY";
                        break;
                    case 4:
                        dayOfWeekName = "THURSDAY";
                        break;
                    case 5:
                        dayOfWeekName = "FRIDAY";
                        break;
                    case 6:
                        dayOfWeekName = "SATURDAY";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromTime = getTimeFormat(fromTimePicker);
                toTime = getTimeFormat(toTimePicker);
                listener.onComplete( dayOfWeekIndex + "," + dayOfWeekName + " - " + fromTime + " ~ " + toTime);
                getDialog().dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    private String getTimeFormat(TimePicker timePicker) {
        int hourOfDay = timePicker.getCurrentHour();
        String status = "AM";
        if (hourOfDay > 11){
            status = "PM";
        }
        int hourFormat;
        if (hourOfDay > 11){
            hourFormat = hourOfDay - 12;
        }
        else{
            hourFormat = hourOfDay;
        }
        int minute = timePicker.getCurrentMinute();

        String format = String.valueOf(hourFormat) + " : " + String.valueOf(minute) + " : " + status;
        return format;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            this.listener = (OnCompleteListener) getTargetFragment();
        }
        catch (final ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }
}
