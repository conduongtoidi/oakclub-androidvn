package com.oakclub.android.model;

public class SettingObject {
    private String filter_male="on";
    private String filter_female="";
    private boolean include_friends=false;
    private int age_from=16;
    private int age_to=32;
    private int range=400;
    
    public String getFilter_male() {
        return filter_male;
    }
    public void setFilter_male(String filter_male) {
        this.filter_male = filter_male;
    }
    public String getFilter_female() {
        return filter_female;
    }
    public void setFilter_female(String filter_female) {
        this.filter_female = filter_female;
    }
    public boolean isInclude_friends() {
        return include_friends;
    }
    public void setInclude_friends(boolean include_friends) {
        this.include_friends = include_friends;
    }
    public int getAge_from() {
        return age_from;
    }
    public void setAge_from(int age_from) {
        this.age_from = age_from;
    }
    public int getAge_to() {
        return age_to;
    }
    public void setAge_to(int age_to) {
        this.age_to = age_to;
    }
    public int getRange() {
        return range;
    }
    public void setRange(int range) {
        this.range = range;
    }
}
