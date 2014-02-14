package com.oakclub.android.model;

import java.io.Serializable;

public class FacebookInfoObject implements Serializable {
    private double id;
    private String name;
    private String avatar;
    public double getId() {
        return id;
    }
    public void setId(double id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
