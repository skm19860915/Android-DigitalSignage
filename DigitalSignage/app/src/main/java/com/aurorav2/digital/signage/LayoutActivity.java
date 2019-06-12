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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.aurorav2.digital.signage.ConfigModel.ContentConfigModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class LayoutActivity extends AppCompatActivity implements ConfigDialog.OnCompleteListener{
    private int whatLayout;
    private String projectName;
    private LinearLayout updateLayout;

    private LinearLayout lhfLayout;

    private LinearLayout lhsLeftLayout;
    private LinearLayout lhsRightLayout;
    private LinearLayout lhsBottomLayout;

    private LinearLayout lhtLeftLayout;
    private LinearLayout lhtRightLayout;
    private LinearLayout lhtBottomLayout;

    private LinearLayout lvfLayout;

    private LinearLayout lvsTopLayout;
    private LinearLayout lvsMiddleLayout;
    private LinearLayout lvsBottomLayout;

    private LinearLayout lvtTopLayout;
    private LinearLayout lvtMiddleLayout;
    private LinearLayout lvtBottomLayout;

    private LinearLayout shfLayout;

    private LinearLayout shsTopLayout;
    private LinearLayout shsBottomLayout;
    private LinearLayout shsRightLayout;

    private LinearLayout svfLayout;

    private LinearLayout svsLeftLayout;
    private LinearLayout svsRightLayout;
    private LinearLayout svsBottomLayout;

    private ArrayList<ContentConfigModel> configList;
    private String savingPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        whatLayout = intent.getIntExtra("whatLayout", 0);
        projectName = intent.getStringExtra("projectName");
        configList = new ArrayList<>();
        savingPath = getFilesDir() + "/Configs/";

        switch (whatLayout){
            case 11:
                loadLHFirstComponent();
                break;
            case 12:
                loadLHSecondComponent();
                break;
            case 13:
                loadLHThirdComponent();
                break;
            case 21:
                loadLVFirstComponent();
                break;
            case 22:
                loadLVSecondComponent();
                break;
            case 23:
                loadLVThirdComponent();
                break;
            case 31:
                loadSHFirstComponent();
                break;
            case 32:
                loadSHSecondComponent();
                break;
            case 41:
                loadSVFirstComponent();
                break;
            case 42:
                loadSVSecondComponent();
                break;
        }
    }

    private void loadLHFirstComponent() {
        setContentView(R.layout.layout_lhf);
        lhfLayout = findViewById(R.id.lhfLayout);
        Button lhfSaveButton = findViewById(R.id.lhfSaveButton);

        lhfLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(1);
            }
        });

        lhfSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfigs(configList);
            }
        });
    }

    private void loadLHSecondComponent() {
        setContentView(R.layout.layout_lhs);
        lhsLeftLayout = findViewById(R.id.lhsLeftLayout);
        lhsRightLayout = findViewById(R.id.lhsRightLayout);
        lhsBottomLayout = findViewById(R.id.lhsBottomLayout);
        Button lhsSaveButton = findViewById(R.id.lhsSaveButton);

        lhsLeftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(1);
            }
        });

        lhsRightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(2);
            }
        });

        lhsBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(3);
            }
        });

        lhsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfigs(configList);
            }
        });
    }

    private void loadLHThirdComponent() {
        setContentView(R.layout.layout_lht);
        lhtLeftLayout = findViewById(R.id.lhtLeftLayout);
        lhtRightLayout = findViewById(R.id.lhtRightLayout);
        lhtBottomLayout = findViewById(R.id.lhtBottomLayout);
        Button lhtSaveButton = findViewById(R.id.lhtSaveButton);

        lhtLeftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(1);
            }
        });

        lhtRightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(2);
            }
        });

        lhtBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(3);
            }
        });

        lhtSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfigs(configList);
            }
        });
    }

    private void loadLVFirstComponent(){
        setContentView(R.layout.layout_lvf);
        lvfLayout = findViewById(R.id.lvfLayout);
        Button lvfSaveButton = findViewById(R.id.lvfSaveButton);

        lvfLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(1);
            }
        });

        lvfSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfigs(configList);
            }
        });
    }

    private void loadLVSecondComponent(){
        setContentView(R.layout.layout_lvs);
        lvsTopLayout = findViewById(R.id.lvsTopLayout);
        lvsMiddleLayout = findViewById(R.id.lvsMiddleLayout);
        lvsBottomLayout = findViewById(R.id.lvsBottomLayout);
        Button lvsSaveButton = findViewById(R.id.lvsSaveButton);

        lvsTopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(1);
            }
        });

        lvsMiddleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(2);
            }
        });

        lvsBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(3);
            }
        });

        lvsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfigs(configList);
            }
        });
    }

    private void loadLVThirdComponent(){
        setContentView(R.layout.layout_lvt);
        lvtTopLayout = findViewById(R.id.lvtTopLayout);
        lvtMiddleLayout = findViewById(R.id.lvtMiddleLayout);
        lvtBottomLayout = findViewById(R.id.lvtBottomLayout);
        Button lvtSaveButton = findViewById(R.id.lvtSaveButton);

        lvtTopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(1);
            }
        });

        lvtMiddleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(2);
            }
        });

        lvtBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(3);
            }
        });

        lvtSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfigs(configList);
            }
        });
    }

    private void loadSHFirstComponent(){
        setContentView(R.layout.layout_shf);
        shfLayout = findViewById(R.id.shfLayout);
        Button shfSaveButton = findViewById(R.id.shfSaveButton);

        shfLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(1);
            }
        });

        shfSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfigs(configList);
            }
        });
    }

    private void loadSHSecondComponent(){
        setContentView(R.layout.layout_shs);
        shsTopLayout = findViewById(R.id.shsTopLayout);
        shsBottomLayout = findViewById(R.id.shsBottomLayout);
        shsRightLayout = findViewById(R.id.shsRightLayout);
        Button shsSaveButton = findViewById(R.id.shsSaveButton);

        shsTopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(1);
            }
        });

        shsBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(2);
            }
        });

        shsRightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(3);
            }
        });

        shsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfigs(configList);
            }
        });
    }

    private void loadSVFirstComponent(){
        setContentView(R.layout.layout_svf);
        svfLayout = findViewById(R.id.svfLayout);
        Button svfSaveButton = findViewById(R.id.svfSaveButton);

        svfLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(1);
            }
        });

        svfSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfigs(configList);
            }
        });
    }

    private void loadSVSecondComponent(){
        setContentView(R.layout.layout_svs);
        svsLeftLayout = findViewById(R.id.svsLeftLayout);
        svsRightLayout = findViewById(R.id.svsRightLayout);
        svsBottomLayout = findViewById(R.id.svsBottomLayout);
        Button svsSaveButton = findViewById(R.id.svsSaveButton);

        svsLeftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(1);
            }
        });

        svsRightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(2);
            }
        });

        svsBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfigDialog(3);
            }
        });

        svsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfigs(configList);
            }
        });
    }

    private void showConfigDialog(int whichFragment) {
        Bundle bundle = new Bundle();
        bundle.putString("projectName", projectName);
        bundle.putInt("whatLayout", whatLayout);
        bundle.putInt("whichFragment", whichFragment);
        ConfigDialog dialog = new ConfigDialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "");
    }

    private void saveConfigs(ArrayList<ContentConfigModel> list) {
        if (list == null || list.size() <= 0){
            Toasty.error(this, "No have content!", Toast.LENGTH_SHORT, true).show();
            return;
        }
        File dir = new File(savingPath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(savingPath + projectName + ".txt");
        if (file.exists() && file.isFile())
            file.delete();
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            String projectName = list.get(0).getProjectName();
            int whatLayout = list.get(0).getWhatLayout();
            bufferedWriter.write(projectName + "=" + whatLayout + "\n");
            for (int i = 0; i < list.size(); i++){
                ContentConfigModel model = list.get(i);
                int whichFragment = model.getWhichFragment();
                int whichType = model.getWhichType();
                StringBuilder builder = new StringBuilder(whichFragment + "=" + whichType);
                ArrayList<String> params = model.getParams();
                for (int j = 0; j < params.size(); j++){
                    String data = "=" + params.get(j);
                    builder.append(data);
                }
                bufferedWriter.write(builder.toString() + "\n");
            }
            bufferedWriter.close();

            Intent intent = new Intent(LayoutActivity.this, PaneActivity.class);
            startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
            Toasty.error(this, "Failed to Save File!", Toast.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public void onComplete(ContentConfigModel model) {
        int whatLayout = model.getWhatLayout();
        int whichFragment = model.getWhichFragment();
        int whichType = model.getWhichType();
        ArrayList<String> params = model.getParams();

        configList.add(model);

        String target = String.valueOf(whatLayout) + String.valueOf(whichFragment);
        switch (target){
            case "111":
                updateLayout = lhfLayout;
                break;
            case "121":
                updateLayout = lhsLeftLayout;
                break;
            case "122":
                updateLayout = lhsRightLayout;
                break;
            case "123":
                updateLayout = lhsBottomLayout;
                break;
            case "131":
                updateLayout = lhtLeftLayout;
                break;
            case "132":
                updateLayout = lhtRightLayout;
                break;
            case "133":
                updateLayout = lhtBottomLayout;
                break;
            case "211":
                updateLayout = lvfLayout;
                break;
            case "221":
                updateLayout = lvsTopLayout;
                break;
            case "222":
                updateLayout = lvsMiddleLayout;
                break;
            case "223":
                updateLayout = lvsBottomLayout;
                break;
            case "231":
                updateLayout = lvtTopLayout;
                break;
            case "232":
                updateLayout = lvtMiddleLayout;
                break;
            case "233":
                updateLayout = lvtBottomLayout;
                break;
            case "311":
                updateLayout = shfLayout;
                break;
            case "321":
                updateLayout = shsTopLayout;
                break;
            case "322":
                updateLayout = shsBottomLayout;
                break;
            case "323":
                updateLayout = shsRightLayout;
                break;
            case "411":
                updateLayout = svfLayout;
                break;
            case "421":
                updateLayout = svsLeftLayout;
                break;
            case "422":
                updateLayout = svsRightLayout;
                break;
            case "423":
                updateLayout = svsBottomLayout;
                break;
        }
        switch (whichType){
            case 1:
                makeTextView(params);
                break;
            case 2:
                makeMediaView(params);
                break;
            case 3:
                makeWebView(params);
                break;
        }
    }

    private void makeTextView(final ArrayList<String> configs) {
        updateLayout.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                int layoutWidth = updateLayout.getWidth();
                TextView textView = new TextView(LayoutActivity.this);
                String textContent = configs.get(0);
                int fontSize = Integer.parseInt(configs.get(1));
                int textColor = Integer.parseInt(configs.get(2));
                int backgroundColor = Integer.parseInt(configs.get(3));
                int directionValue = Integer.parseInt(configs.get(4));

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
                updateLayout.addView(textView);
            }
        });
    }

    private void makeMediaView(ArrayList<String> configs) {
        //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String path = "/mnt/sdcard/Download/";
        String isSelectedOfPathListView = configs.get(0);
        if (isSelectedOfPathListView.equals("list")){
            String fileName = configs.get(1);
            File file = new File(path + "/" + fileName);
            String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
            if (extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png")){
                final ImageView imageView = new ImageView(LayoutActivity.this);
                updateLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        int layoutWidth = updateLayout.getWidth();
                        int layoutHeight = updateLayout.getHeight();
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(layoutWidth, layoutHeight);
                        layoutParams.gravity = Gravity.CENTER;
                        imageView.setLayoutParams(layoutParams);
                    }
                });
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                    imageView.setImageBitmap(bitmap);
                    updateLayout.addView(imageView);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if (extension.equals(".mp4")){
                VideoView videoView = new VideoView(this);
                videoView.setVideoPath(path + "/" + fileName);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                videoView.setLayoutParams(params);
                videoView.start();
                updateLayout.addView(videoView);
            }
        }
        else{
            String url = configs.get(1);
            Uri uri = Uri.parse(url);
            VideoView videoView = new VideoView(this);
            videoView.setVideoURI(uri);
            videoView.start();
            updateLayout.addView(videoView);
        }
    }

    private void makeWebView(ArrayList<String> configs) {
        WebView webView = new WebView(LayoutActivity.this);
        String url = configs.get(0);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
        updateLayout.addView(webView);
    }
}
