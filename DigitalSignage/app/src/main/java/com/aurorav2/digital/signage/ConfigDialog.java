package com.aurorav2.digital.signage;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aurorav2.digital.signage.ConfigModel.ContentConfigModel;

import java.io.File;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import yuku.ambilwarna.AmbilWarnaDialog;

public class ConfigDialog extends DialogFragment {
    private Button cancelButton;
    private Button okButton;
    private Spinner objectSpinner;
    private String fileTypes[] = {"Scrolling Text", "Picture and Video", "WebPage"};
    private ArrayAdapter<String> fileTypeAdapter;

    private String projectName;
    private int whatLayout;
    private int whichFragment;
    private int initConfig;
    private String fileName;

    private LinearLayout textLinearLayout;
    private EditText textContentEditText;
    private EditText fontSizeEditText;
    private TextView textColorDrawTextView;
    private int textDefaultColor;
    private TextView backgroundColorDrawTextView;
    private int backgroundDefaultColor;
    private Spinner directionSpinner;
    private String directions[] = {"Right to left", "Left to right"};
    private ArrayAdapter<String> directionAdapter;
    private Spinner speedSpinner;
    private String speeds[] = {"Medium low", "Fast", "Slow"};
    private ArrayAdapter<String> speedAdapter;
    private int directionValue;

    private LinearLayout mediaLinearLayout;
    private RadioGroup selectRadioGroup;
    private ListView pathListView;
    private EditText videoUrlEditText;
    private Spinner effectsSpinner;
    private String effects[] = {"Fade in/out", "Fade out/in"};
    private ArrayAdapter<String> effectsAdapter;
    private Spinner switchingSpeedSpinner;
    private String switchingSpeeds[] = {"5 secs", "10 secs", "30 secs"};
    private ArrayAdapter<String> switchingSpeedAdapter;
    private String isSelectedOfPathListView;

    private LinearLayout webLinearLayout;
    private EditText webPageEditText;

    private Context context;

    private OnCompleteListener mListener;

    public interface OnCompleteListener{
        void onComplete(ContentConfigModel model);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_config, container, false);

        projectName = getArguments().getString("projectName");
        whatLayout = getArguments().getInt("whatLayout");
        whichFragment = getArguments().getInt("whichFragment");

        loadComponentOfTextLinearLayout(view);
        loadComponentOfMediaLinearLayout(view);
        loadComponentOfWebLinearLayout(view);
        cancelButton = view.findViewById(R.id.cancelButton);
        okButton = view.findViewById(R.id.okButton);
        textLinearLayout = view.findViewById(R.id.textLinearLayout);
        mediaLinearLayout = view.findViewById(R.id.mediaLinearLayout);
        webLinearLayout = view.findViewById(R.id.webLinearLayout);
        objectSpinner = view.findViewById(R.id.objectSpinner);

        fileTypeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, fileTypes);
        objectSpinner.setAdapter(fileTypeAdapter);
        initDialog(0);
        objectSpinner.setSelection(0);

        objectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        initConfig = 0;
                        textLinearLayout.setVisibility(View.VISIBLE);
                        mediaLinearLayout.setVisibility(View.GONE);
                        webLinearLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        initConfig = 1;
                        textLinearLayout.setVisibility(View.GONE);
                        mediaLinearLayout.setVisibility(View.VISIBLE);
                        webLinearLayout.setVisibility(View.GONE);
                        break;
                    case 2:
                        initConfig = 2;
                        textLinearLayout.setVisibility(View.GONE);
                        mediaLinearLayout.setVisibility(View.GONE);
                        webLinearLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        initConfig = 0;
                        textLinearLayout.setVisibility(View.VISIBLE);
                        mediaLinearLayout.setVisibility(View.GONE);
                        webLinearLayout.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            boolean successOfSavingConfigs;
            @Override
            public void onClick(View v) {
                switch (initConfig){
                    case 0:
                        successOfSavingConfigs = saveConfigOfTextView();
                        break;
                    case 1:
                        successOfSavingConfigs = saveConfigOfMediaView();
                        break;
                    case 2:
                        successOfSavingConfigs = saveConfigOfWebView();
                        break;
                }
                if (successOfSavingConfigs){ getDialog().dismiss(); }
            }
        });
        return view;
    }

    private boolean saveConfigOfTextView() {
        String textContent = textContentEditText.getText().toString();
        if (textContent.isEmpty()){
            Toasty.error(context, "Please input text !", Toast.LENGTH_LONG, true).show();
            return false;
        }
        String fontSize = fontSizeEditText.getText().toString();
        if (fontSize.isEmpty()){
            Toasty.error(context, "Please input font size !", Toast.LENGTH_LONG, true).show();
            return false;
        }

        ContentConfigModel model = new ContentConfigModel();
        model.setProjectName(projectName);
        model.setWhatLayout(whatLayout);
        model.setWhichFragment(whichFragment);
        model.setWhichType(1);
        ArrayList<String> params = new ArrayList<>();
        params.add(textContent);
        params.add(fontSize);
        params.add(String.valueOf(textDefaultColor));
        params.add(String.valueOf(backgroundDefaultColor));
        params.add(String.valueOf(directionValue));
        model.setParams(params);

        mListener.onComplete(model);
        return true;
    }

    private boolean saveConfigOfMediaView() {
        if (isSelectedOfPathListView.equals("list")){
            if (fileName == null){
                Toasty.error(context, "Please select file!", Toast.LENGTH_LONG, true).show();
                return false;
            }
        }
        else{
            String url = videoUrlEditText.getText().toString();
            if (url == null || url.isEmpty()){
                Toasty.error(context, "Please input video url!", Toast.LENGTH_LONG, true).show();
                return false;
            }
            fileName = url;
        }

        ContentConfigModel model = new ContentConfigModel();
        model.setProjectName(projectName);
        model.setWhatLayout(whatLayout);
        model.setWhichFragment(whichFragment);
        model.setWhichType(2);
        ArrayList<String> params = new ArrayList<>();
        params.add(isSelectedOfPathListView);
        params.add(fileName);
        model.setParams(params);

        mListener.onComplete(model);
        return true;
    }

    private boolean saveConfigOfWebView() {
        String url = webPageEditText.getText().toString();
        if (url.isEmpty()){
            Toasty.error(context, "Please input page url !", Toast.LENGTH_LONG, true).show();
            return false;
        }

        ContentConfigModel model = new ContentConfigModel();
        model.setProjectName(projectName);
        model.setWhatLayout(whatLayout);
        model.setWhichFragment(whichFragment);
        model.setWhichType(3);
        ArrayList<String> params = new ArrayList<>();
        params.add(url);
        model.setParams(params);

        mListener.onComplete(model);
        return true;
    }

    private void initDialog(int initConfig) {
        switch (initConfig){
            case 0:
                textLinearLayout.setVisibility(View.VISIBLE);
                mediaLinearLayout.setVisibility(View.GONE);
                webLinearLayout.setVisibility(View.GONE);
                break;
            case 1:
                textLinearLayout.setVisibility(View.GONE);
                mediaLinearLayout.setVisibility(View.VISIBLE);
                webLinearLayout.setVisibility(View.GONE);
                break;
            case 2:
                textLinearLayout.setVisibility(View.GONE);
                mediaLinearLayout.setVisibility(View.GONE);
                webLinearLayout.setVisibility(View.VISIBLE);
                break;
            default:
                textLinearLayout.setVisibility(View.VISIBLE);
                mediaLinearLayout.setVisibility(View.GONE);
                webLinearLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void loadComponentOfWebLinearLayout(View view) {
        webPageEditText = view.findViewById(R.id.webPageEditText);
    }

    private void loadComponentOfTextLinearLayout(View view) {
        textContentEditText = view.findViewById(R.id.textContentEditText);
        fontSizeEditText = view.findViewById(R.id.fontSizeEditText);
        textColorDrawTextView = view.findViewById(R.id.textColorDrawTextView);
        textDefaultColor = ContextCompat.getColor(context, R.color.colorLightGreen);
        textColorDrawTextView.setBackgroundColor(textDefaultColor);
        textColorDrawTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker(textColorDrawTextView);
            }
        });
        backgroundColorDrawTextView = view.findViewById(R.id.backgroundColorDrawTextView);
        backgroundDefaultColor = ContextCompat.getColor(context, R.color.colorBlack);
        backgroundColorDrawTextView.setBackgroundColor(backgroundDefaultColor);
        backgroundColorDrawTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackgroundColorPicker(backgroundColorDrawTextView);
            }
        });
        directionSpinner = view.findViewById(R.id.rollingDirectionSpinner);
        directionAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, directions);
        directionSpinner.setAdapter(directionAdapter);
        directionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        directionValue = 0;
                        break;
                    case 1:
                        directionValue = 1;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        speedSpinner = view.findViewById(R.id.rollingSpeedSpinner);
        speedAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, speeds);
        speedSpinner.setAdapter(speedAdapter);
        speedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadComponentOfMediaLinearLayout(View view) {
        selectRadioGroup = view.findViewById(R.id.selectRadioGroup);
        videoUrlEditText = view.findViewById(R.id.videoUrlEditText);
        pathListView = view.findViewById(R.id.pathListView);
        effectsSpinner = view.findViewById(R.id.effectsSpinner);
        switchingSpeedSpinner = view.findViewById(R.id.switchingSpeedSpinner);
        pathListView.setEnabled(true);
        videoUrlEditText.setEnabled(false);
        isSelectedOfPathListView = "list";
        selectRadioGroup.check(R.id.fileListRadioButton);

        selectRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                switch (id){
                    case R.id.fileListRadioButton:
                        pathListView.setEnabled(true);
                        videoUrlEditText.setEnabled(false);
                        isSelectedOfPathListView = "list";
                        break;
                    case R.id.videoUrlRadioButton:
                        pathListView.setEnabled(false);
                        videoUrlEditText.setEnabled(true);
                        isSelectedOfPathListView = "url";
                        break;
                }
            }
        });

        //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String path = "/mnt/sdcard/Download/";
        File dir = new File(path);
        final File[] files = dir.listFiles();
        if (files != null && files.length > 0){
            FileListAdapter adapter = new FileListAdapter(context, files.length, files);
            pathListView.setAdapter(adapter);
            pathListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setSelected(true);
                    fileName = files[position].getName();
                }
            });
        }

        effectsAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, effects);
        effectsSpinner.setAdapter(effectsAdapter);
        effectsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switchingSpeedAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, switchingSpeeds);
        switchingSpeedSpinner.setAdapter(switchingSpeedAdapter);
        switchingSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void openColorPicker(final TextView view) {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(context, textDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                textDefaultColor = color;
                view.setBackgroundColor(textDefaultColor);
            }
        });
        ambilWarnaDialog.show();
    }

    private void openBackgroundColorPicker(final TextView view) {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(context, backgroundDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                backgroundDefaultColor = color;
                view.setBackgroundColor(backgroundDefaultColor);
            }
        });
        ambilWarnaDialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try{
            this.mListener = (OnCompleteListener) context;
        }
        catch (final ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }
}
