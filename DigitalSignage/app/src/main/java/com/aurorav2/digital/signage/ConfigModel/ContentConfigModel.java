package com.aurorav2.digital.signage.ConfigModel;

import java.util.ArrayList;

public class ContentConfigModel {
    private String projectName;
    private int whatLayout;
    private int whichFragment;
    private int whichType;
    private ArrayList<String> params;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getWhatLayout() {
        return whatLayout;
    }

    public void setWhatLayout(int whatLayout) {
        this.whatLayout = whatLayout;
    }

    public int getWhichFragment() {
        return whichFragment;
    }

    public void setWhichFragment(int whichFragment) {
        this.whichFragment = whichFragment;
    }

    public int getWhichType() {
        return whichType;
    }

    public void setWhichType(int whichType) {
        this.whichType = whichType;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }
}
