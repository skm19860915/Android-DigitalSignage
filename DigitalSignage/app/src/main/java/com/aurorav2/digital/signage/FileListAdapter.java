package com.aurorav2.digital.signage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileListAdapter extends ArrayAdapter<String> {
    private int count;
    private Context context;
    private File[] files;

    public FileListAdapter(Context context, int count, File[] files){
        super(context, R.layout.file_item);
        this.context = context;
        this.files = files;
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
            convertView = inflater.inflate(R.layout.file_item, parent, false);
            holder.targetImageView = convertView.findViewById(R.id.targetImageView);
            holder.fileNameTextView = convertView.findViewById(R.id.fileNameTextView);
            holder.dateTextView = convertView.findViewById(R.id.dateTextView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        File file = files[position];
        String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
        if (extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png")){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                int nh = (int) (bitmap.getHeight() * (32.0/bitmap.getWidth()));
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 32, nh, true);
                holder.targetImageView.setImageBitmap(scaledBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (extension.equals(".mp4")){
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.play);
            int nh = (int) (bitmap.getHeight() * (32.0/bitmap.getWidth()));
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 32, nh, true);
            holder.targetImageView.setImageBitmap(scaledBitmap);
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(file.lastModified());
        holder.fileNameTextView.setText(file.getName());
        holder.dateTextView.setText(dateFormat.format(date));

        return convertView;
    }

    static class ViewHolder{
        ImageView targetImageView;
        TextView fileNameTextView;
        TextView dateTextView;
    }
}
