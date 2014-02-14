package com.oakclub.android.model;

import java.util.ArrayList;

public class ListChatReturnObject {
    private boolean status;
    private ArrayList<ListChatData> data;
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public ArrayList<ListChatData> getData() {
        return data;
    }
    public void setData(ArrayList<ListChatData> data) {
        this.data = data;
    }
}
