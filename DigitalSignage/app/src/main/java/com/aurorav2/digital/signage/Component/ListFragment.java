package com.aurorav2.digital.signage.Component;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aurorav2.digital.signage.ProjectActivity;
import com.aurorav2.digital.signage.ProjectListAdapter;
import com.aurorav2.digital.signage.R;

import java.io.File;

public class ListFragment extends Fragment {
    private String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ListView listView = rootView.findViewById(R.id.projectListView);
        String rootPath = getContext().getFilesDir() + "/Configs/";
        File dir = new File(rootPath);
        if (!dir.exists()){
            return rootView;
        }
        final File[] files = dir.listFiles();
        if (files == null || files.length <= 0){
            return rootView;
        }
        ProjectListAdapter adapter = new ProjectListAdapter(getContext(), files.length, files);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                path = files[position].getPath();
                Intent intent = new Intent(getContext(), ProjectActivity.class);
                intent.putExtra("path", path);
                intent.putExtra("index", -1);
                startActivity(intent);
            }
        });
        return rootView;
    }
}

