package com.aurorav2.digital.signage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

public class AutoRunActivity extends AppCompatActivity {
    String path;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_run);

        Intent getIndent = getIntent();
        int selectedIndex = getIndent.getIntExtra("selectedIndex", -1);

        String configPath = getFilesDir() + "/Configs/";
        File dir = new File(configPath);
        if (!dir.exists())
            return;
        final File[] files = dir.listFiles();
        if (files == null || files.length <= 0)
            return;
        for (int i = 0; i < files.length; i++){
            if (selectedIndex == files.length - 1){
                Intent intent = new Intent(AutoRunActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            }
            if (selectedIndex < i){
                path = files[i].getPath();
                index = i;
                break;
            }
        }

        if (selectedIndex < files.length - 1){
            Intent intent = new Intent(AutoRunActivity.this, ProjectActivity.class);
            intent.putExtra("path", path);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }
}
