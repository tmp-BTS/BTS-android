package com.example.bts.Adapter;

public class HistoryListItem {
    private String time;
    private String temperature;
    private String name;

    public void setTime(String mTime){
        this.time = mTime;
    }

    public void setName(String mName){
        this.name = mName;
    }

    public void setTemperature(String mTemperature){
        this.temperature = mTemperature;
    }

    public String getTime(){
        return this.time;
    }

    public String getName(){
        return this.name;
    }

    public String getTemperature(){
        return this.temperature;
    }

}
