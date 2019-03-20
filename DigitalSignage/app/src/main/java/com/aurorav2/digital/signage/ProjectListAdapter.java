package com.aurorav2.digital.signage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectListAdapter extends ArrayAdapter<String> {
    private int count;
    private Context context;
    private File[] projects;

    public ProjectListAdapter(Context context, int count, File[] projects){
        super(context, R.layout.project_item);
        this.context = context;
        this.projects = projects;
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.project_item, parent, false);
            holder.projectImageView = convertView.findViewById(R.id.projectImageView);
            holder.projectNameTextView = convertView.findViewById(R.id.projectNameTextView);
            holder.projectDateTextView = convertView.findViewById(R.id.projectDateTextView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        File file = projects[position];
        int whatLayout = getFileInformation(file);
        int drawableValue = getImage(whatLayout);

        holder.projectImageView.setImageResource(drawableValue);
        holder.projectImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(0, pos);
        }
        holder.projectNameTextView.setText(fileName);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(file.lastModified());
        holder.projectDateTextView.setText(dateFormat.format(date));

        return convertView;
    }

    private int getImage(int whatLayout) {
        int drawableValue = -1;
        switch (whatLayout){
            case 11:
                drawableValue = R.drawable.lhf;
                break;
            case 12:
                drawableValue = R.drawable.lhs;
                break;
            case 13:
                drawableValue = R.drawable.lht;
                break;
            case 21:
                drawableValue = R.drawable.lvf;
                break;
            case 22:
                drawableValue = R.drawable.lvs;
                break;
            case 23:
                drawableValue = R.drawable.lvt;
                break;
            case 31:
                drawableValue = R.drawable.shf;
                break;
            case 32:
                drawableValue = R.drawable.shs;
                break;
            case 41:
                drawableValue = R.drawable.svf;
                break;
            case 42:
                drawableValue = R.drawable.svs;
                break;

        }
        return drawableValue;
    }

    private int getFileInformation(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            try {
                String line = reader.readLine();
                String[] strs = line.split("=");
                return Integer.parseInt(strs[1]);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    static class ViewHolder{
        ImageView projectImageView;
        TextView projectNameTextView;
        TextView projectDateTextView;
    }
}
