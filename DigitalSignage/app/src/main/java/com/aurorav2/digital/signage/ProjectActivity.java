package com.aurorav2.digital.signage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ProjectActivity extends AppCompatActivity {
    private LinearLayout contentLayout;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        contentLayout = findViewById(R.id.contentLayout);

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        final int index = intent.getIntExtra("index", 0);
        File file = new File(path);
        ArrayList<String > list = getConfigInformationFromFile(file);
        if (list == null || list.size() <= 0){
            return;
        }
        generateLayoutTemplate(list);

        if(index > -1){
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(ProjectActivity.this, AutoRunActivity.class);
                    intent.putExtra("selectedIndex", index);
                    startActivity(intent);
                    finish();
                }
            }, 10000);
        }
    }

    private void generateContent(String string, LinearLayout layout) {
        String[] infos = string.split("=");
        int whichType = Integer.parseInt(infos[1]);
        switch (whichType){
            case 1:
                generateTextViewContent(infos, layout);
                break;
            case 2:
                generateMediaContent(infos, layout);
                break;
            case 3:
                generateWebContent(infos, layout);
                break;
        }
    }

    private void generateWebContent(String[] infos, LinearLayout layout) {
        WebView webView = new WebView(ProjectActivity.this);
        String url = infos[2];
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
        layout.addView(webView);
    }

    private void generateMediaContent(String[] infos, final LinearLayout layout) {
        //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String path = "/mnt/sdcard/Download/";
        String isSelectedOfPathListView = infos[2];
        if (isSelectedOfPathListView.equals("list")){
            String fileName = infos[3];
            File file = new File(path + "/" + fileName);
            String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
            if (extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png")){
                final ImageView imageView = new ImageView(ProjectActivity.this);
                layout.post(new Runnable() {
                    @Override
                    public void run() {
                        int layoutWidth = layout.getWidth();
                        int layoutHeight = layout.getHeight();
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(layoutWidth, layoutHeight);
                        layoutParams.gravity = Gravity.CENTER;
                        imageView.setLayoutParams(layoutParams);
                    }
                });
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                    imageView.setImageBitmap(bitmap);
                    layout.addView(imageView);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if (extension.equals(".mp4")){
                VideoView videoView = new VideoView(this);
                MediaController mediaController = new MediaController(this);
                videoView.setVideoPath(path + "/" + fileName);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                videoView.setLayoutParams(params);
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);
                videoView.start();
                layout.addView(videoView);
            }
        }
        else{
            String url = infos[3];
            Uri uri = Uri.parse(url);
            MediaController mediaController = new MediaController(this);
            VideoView videoView = new VideoView(this);
            videoView.setVideoURI(uri);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            videoView.start();
            layout.addView(videoView);
        }
    }

    private void generateTextViewContent(final String[] infos, final LinearLayout layout) {
        layout.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                int layoutWidth = layout.getWidth();
                TextView textView = new TextView(ProjectActivity.this);
                String textContent = infos[2];
                int fontSize = Integer.parseInt(infos[3]);
                int textColor = Integer.parseInt(infos[4]);
                int backgroundColor = Integer.parseInt(infos[5]);
                int directionValue = Integer.parseInt(infos[6]);

                textView.append(textContent);
                textView.setTextSize(fontSize);
                textView.setTextColor(textColor);
                textView.setBackgroundColor(backgroundColor);
                if (directionValue == 0){
                    textView.setTextDirection(View.TEXT_DIRECTION_LTR);
                }
                else{
                    textView.setTextDirection(View.TEXT_DIRECTION_RTL);
                }
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setGravity(Gravity.CENTER);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.setSelected(true);
                textView.setSingleLine(true);
                textView.setHorizontallyScrolling(true);
                textView.setMarqueeRepeatLimit(-1);
                textView.measure(0, 0);
                int textWidth = textView.getMeasuredWidth();
                int textLength = textView.getText().toString().length();

                Rect bounds = new Rect();
                Paint paint = textView.getPaint();
                paint.getTextBounds("a", 0, 1, bounds);
                int characterWidth = bounds.width();
                int length = layoutWidth / characterWidth;

                if (layoutWidth < textWidth){
                    textView.setText(textView.getText().toString());
                }
                else{
                    String repeated = new String(new char[100]).replace("\0", " ");
                    textView.setText( textView.getText().toString() +  repeated);
                }
                layout.addView(textView);
            }
        });
    }

    private void generateLayoutTemplate(ArrayList<String> list) {
        String headLine = list.get(0);
        String[] firstConfigs = headLine.split("=");
        int whatLayout = Integer.parseInt(firstConfigs[1]);
        switch (whatLayout){
            case 11:
                generateLHFLayoutTemplate(list);
                break;
            case 12:
                generateLHSLayoutTemplate(list);
                break;
            case 13:
                generateLHTLayoutTemplate(list);
                break;
            case 21:
                generateLVFLayoutTemplate(list);
                break;
            case 22:
                generateLVSLayoutTemplate(list);
                break;
            case 23:
                generateLVTLayoutTemplate(list);
                break;
            case 31:
                generateSHFLayoutTemplate(list);
                break;
            case 32:
                generateSHSLayoutTemplate(list);
                break;
            case 41:
                generateSVFLayoutTemplate(list);
                break;
            case 42:
                generateSVSLayoutTemplate(list);
                break;
        }
    }

    private void generateLHFLayoutTemplate(ArrayList<String> list) {
        LinearLayout lhfLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        lhfLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        generateContent(list.get(1), lhfLayout);
        contentLayout.addView(lhfLayout, params);
    }

    private void generateLHSLayoutTemplate(ArrayList<String> list) {
        LinearLayout lhsLayout = new LinearLayout(this);
        LinearLayout lhsTopLayout = new LinearLayout(this);
        LinearLayout lhsBottomLayout = new LinearLayout(this);
        LinearLayout lhsLeftLayout = new LinearLayout(this);
        LinearLayout lhsRightLayout = new LinearLayout(this);

        LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 9.0f);
        LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 3.0f);
        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 4.0f);

        lhsLeftLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        lhsRightLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        lhsBottomLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));

        for (int i = 1; i < list.size() - 1; i++){
            String[] strings = list.get(i).split("=");
            int whichFragment = Integer.parseInt(strings[0]);
            switch (whichFragment){
                case 1:
                    generateContent(list.get(i), lhsLeftLayout);
                    break;
                case 2:
                    generateContent(list.get(i), lhsRightLayout);
                    break;
                case 3:
                    generateContent(list.get(i), lhsBottomLayout);
                    break;
            }
        }

        lhsTopLayout.setOrientation(LinearLayout.HORIZONTAL);
        lhsTopLayout.addView(lhsLeftLayout, leftParams);
        lhsTopLayout.addView(lhsRightLayout, rightParams);

        lhsLayout.setOrientation(LinearLayout.VERTICAL);
        lhsLayout.addView(lhsTopLayout, topParams);
        lhsLayout.addView(lhsBottomLayout, bottomParams);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;

        contentLayout.addView(lhsLayout, params);
    }

    private void generateLHTLayoutTemplate(ArrayList<String> list) {
        LinearLayout lhtLayout = new LinearLayout(this);
        LinearLayout lhtTopLayout = new LinearLayout(this);
        LinearLayout lhtBottomLayout = new LinearLayout(this);
        LinearLayout lhtLeftLayout = new LinearLayout(this);
        LinearLayout lhtRightLayout = new LinearLayout(this);

        LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 3.0f);
        LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 9.0f);
        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 4.0f);

        lhtLeftLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        lhtRightLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        lhtBottomLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));

        for (int i = 1; i < list.size() - 1; i++){
            String[] strings = list.get(i).split("=");
            int whichFragment = Integer.parseInt(strings[0]);
            switch (whichFragment){
                case 1:
                    generateContent(list.get(i), lhtLeftLayout);
                    break;
                case 2:
                    generateContent(list.get(i), lhtRightLayout);
                    break;
                case 3:
                    generateContent(list.get(i), lhtBottomLayout);
                    break;
            }
        }

        lhtTopLayout.setOrientation(LinearLayout.HORIZONTAL);
        lhtTopLayout.addView(lhtLeftLayout, leftParams);
        lhtTopLayout.addView(lhtRightLayout, rightParams);

        lhtLayout.setOrientation(LinearLayout.VERTICAL);
        lhtLayout.addView(lhtTopLayout, topParams);
        lhtLayout.addView(lhtBottomLayout, bottomParams);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        contentLayout.addView(lhtLayout, params);
    }

    private void generateLVFLayoutTemplate(ArrayList<String> list) {
        LinearLayout lvfLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        lvfLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        generateContent(list.get(1), lvfLayout);
        contentLayout.addView(lvfLayout, params);
    }

    private void generateLVSLayoutTemplate(ArrayList<String> list) {
        LinearLayout lvsLayout = new LinearLayout(this);
        LinearLayout lvsTopLayout = new LinearLayout(this);
        LinearLayout lvsBottomLayout = new LinearLayout(this);
        LinearLayout lvsMiddleLayout = new LinearLayout(this);

        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 2.0f);
        LinearLayout.LayoutParams middleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 2.0f);
        LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 3.0f);

        lvsTopLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        lvsMiddleLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        lvsBottomLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));

        for (int i = 1; i < list.size() - 1; i++){
            String[] strings = list.get(i).split("=");
            int whichFragment = Integer.parseInt(strings[0]);
            switch (whichFragment){
                case 1:
                    generateContent(list.get(i), lvsTopLayout);
                    break;
                case 2:
                    generateContent(list.get(i), lvsMiddleLayout);
                    break;
                case 3:
                    generateContent(list.get(i), lvsBottomLayout);
                    break;
            }
        }

        lvsLayout.setOrientation(LinearLayout.VERTICAL);
        lvsLayout.addView(lvsTopLayout, topParams);
        lvsLayout.addView(lvsMiddleLayout, middleParams);
        lvsLayout.addView(lvsBottomLayout, bottomParams);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        contentLayout.addView(lvsLayout, params);
    }

    private void generateLVTLayoutTemplate(ArrayList<String> list) {
        LinearLayout lvtLayout = new LinearLayout(this);
        LinearLayout lvtTopLayout = new LinearLayout(this);
        LinearLayout lvtBottomLayout = new LinearLayout(this);
        LinearLayout lvtMiddleLayout = new LinearLayout(this);

        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        LinearLayout.LayoutParams middleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);

        lvtTopLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        lvtMiddleLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        lvtBottomLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));

        for (int i = 1; i < list.size() - 1; i++){
            String[] strings = list.get(i).split("=");
            int whichFragment = Integer.parseInt(strings[0]);
            switch (whichFragment){
                case 1:
                    generateContent(list.get(i), lvtTopLayout);
                    break;
                case 2:
                    generateContent(list.get(i), lvtMiddleLayout);
                    break;
                case 3:
                    generateContent(list.get(i), lvtBottomLayout);
                    break;
            }
        }

        lvtLayout.setOrientation(LinearLayout.VERTICAL);
        lvtLayout.addView(lvtTopLayout, topParams);
        lvtLayout.addView(lvtMiddleLayout, middleParams);
        lvtLayout.addView(lvtBottomLayout, bottomParams);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        contentLayout.addView(lvtLayout, params);
    }

    private void generateSHFLayoutTemplate(ArrayList<String> list) {
        LinearLayout shfLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        shfLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        generateContent(list.get(1), shfLayout);
        contentLayout.addView(shfLayout, params);
    }

    private void generateSHSLayoutTemplate(ArrayList<String> list) {
        LinearLayout shsLayout = new LinearLayout(this);
        LinearLayout shsRightLayout = new LinearLayout(this);
        LinearLayout shsLeftLayout = new LinearLayout(this);
        LinearLayout shsTopLayout = new LinearLayout(this);
        LinearLayout shsBottomLayout = new LinearLayout(this);

        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);

        LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 3.0f);
        LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 9.0f);


        shsTopLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        shsBottomLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        shsRightLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));

        for (int i = 1; i < list.size() - 1; i++){
            String[] strings = list.get(i).split("=");
            int whichFragment = Integer.parseInt(strings[0]);
            switch (whichFragment){
                case 1:
                    generateContent(list.get(i), shsTopLayout);
                    break;
                case 2:
                    generateContent(list.get(i), shsBottomLayout);
                    break;
                case 3:
                    generateContent(list.get(i), shsRightLayout);
                    break;
            }
        }

        shsLeftLayout.setOrientation(LinearLayout.VERTICAL);
        shsLeftLayout.addView(shsTopLayout, topParams);
        shsLeftLayout.addView(shsBottomLayout, bottomParams);

        shsLayout.setOrientation(LinearLayout.HORIZONTAL);
        shsLayout.addView(shsLeftLayout, leftParams);
        shsLayout.addView(shsRightLayout, rightParams);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        contentLayout.addView(shsLayout, params);
    }

    private void generateSVFLayoutTemplate(ArrayList<String> list) {
        LinearLayout svfLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        svfLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        generateContent(list.get(1), svfLayout);
        contentLayout.addView(svfLayout, params);
    }

    private void generateSVSLayoutTemplate(ArrayList<String> list) {
        LinearLayout svsLayout = new LinearLayout(this);
        LinearLayout svsTopLayout = new LinearLayout(this);
        LinearLayout svsBottomLayout = new LinearLayout(this);
        LinearLayout svsLeftLayout = new LinearLayout(this);
        LinearLayout svsRightLayout = new LinearLayout(this);

        LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 3.0f);

        svsLeftLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        svsRightLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        svsBottomLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));

        for (int i = 1; i < list.size() - 1; i++){
            String[] strings = list.get(i).split("=");
            int whichFragment = Integer.parseInt(strings[0]);
            switch (whichFragment){
                case 1:
                    generateContent(list.get(i), svsLeftLayout);
                    break;
                case 2:
                    generateContent(list.get(i), svsRightLayout);
                    break;
                case 3:
                    generateContent(list.get(i), svsBottomLayout);
                    break;
            }
        }

        svsTopLayout.setOrientation(LinearLayout.HORIZONTAL);
        svsTopLayout.addView(svsLeftLayout, leftParams);
        svsTopLayout.addView(svsRightLayout, rightParams);

        svsLayout.setOrientation(LinearLayout.VERTICAL);
        svsLayout.addView(svsTopLayout, topParams);
        svsLayout.addView(svsBottomLayout, bottomParams);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        contentLayout.addView(svsLayout, params);
    }

    private ArrayList<String> getConfigInformationFromFile(File file) {
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
}
