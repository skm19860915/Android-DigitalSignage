package com.aurorav2.digital.signage.Component;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.aurorav2.digital.signage.LayoutActivity;
import com.aurorav2.digital.signage.R;

public class LayoutFragment extends Fragment {
    private int whatLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
        final RadioButton lhFirstButton = rootView.findViewById(R.id.lhFirstRadioButton);
        final RadioButton lhSecondButton = rootView.findViewById(R.id.lhSecondRadioButton);
        final RadioButton lhThirdButton = rootView.findViewById(R.id.lhThirdRadioButton);
        final RadioButton lvFirstButton = rootView.findViewById(R.id.lvFirstRadioButton);
        final RadioButton lvSecondButton = rootView.findViewById(R.id.lvSecondRadioButton);
        final RadioButton lvThirdButton = rootView.findViewById(R.id.lvThirdRadioButton);
        final RadioButton shFirstButton = rootView.findViewById(R.id.shFirstRadioButton);
        final RadioButton shSecondButton = rootView.findViewById(R.id.shSecondRadioButton);
        final RadioButton svFirstButton = rootView.findViewById(R.id.svFirstRadioButton);
        final RadioButton svSecondButton = rootView.findViewById(R.id.svSecondRadioButton);

        lhFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatLayout = 11;
                showProjectDialog();
            }
        });
        lhSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatLayout = 12;
                showProjectDialog();
            }
        });
        lhThirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatLayout = 13;
                showProjectDialog();
            }
        });
        lvFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatLayout = 21;
                showProjectDialog();
            }
        });
        lvSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatLayout = 22;
                showProjectDialog();
            }
        });
        lvThirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatLayout = 23;
                showProjectDialog();
            }
        });
        shFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatLayout = 31;
                showProjectDialog();
            }
        });
        shSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatLayout = 32;
                showProjectDialog();
            }
        });
        svFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatLayout = 41;
                showProjectDialog();
            }
        });
        svSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatLayout = 42;
                showProjectDialog();
            }
        });

        return rootView;
    }

    private void showProjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_project, null);
        Button cancelBtn = view.findViewById(R.id.cancelButton);
        Button okBtn = view.findViewById(R.id.okButton);
        final EditText nameEditText = view.findViewById(R.id.nameEditText);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projectName = nameEditText.getText().toString();
                Intent intent = new Intent(getActivity(), LayoutActivity.class);
                intent.putExtra("whatLayout", whatLayout);
                intent.putExtra("projectName", projectName);
                startActivity(intent);
            }
        });
        dialog.show();
    }
}
