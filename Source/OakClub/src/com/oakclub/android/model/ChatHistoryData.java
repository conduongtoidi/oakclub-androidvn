package com.oakclub.android.model;

public class ChatHistoryData {
    private String body ;
    private double time;
    private String time_string;
    private String from;
    private String to;
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public double getTime() {
        return time;
    }
    public void setTime(double time) {
        this.time = time;
    }
    public String getTime_string() {
        return time_string;
    }
    public void setTime_string(String time_string) {
        this.time_string = time_string;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    
}
