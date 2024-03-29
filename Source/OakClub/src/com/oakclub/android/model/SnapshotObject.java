package com.oakclub.android.model;

import java.util.ArrayList;

public class SnapshotObject {
    private boolean status;
    private ArrayList<SnapshotData> data;
    
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public ArrayList<SnapshotData> getData() {
        return data;
    }
    public void setData(ArrayList<SnapshotData> data) {
        this.data = data;
    }
}
