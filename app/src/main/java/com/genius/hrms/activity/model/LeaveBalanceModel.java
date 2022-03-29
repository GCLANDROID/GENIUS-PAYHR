package com.genius.hrms.activity.model;

import android.widget.TextView;

public class LeaveBalanceModel {
    String tvTaken,tvOpening,tvAvailable,tvDate ;

    public LeaveBalanceModel(  String tvDate, String tvOpening,String tvTaken,String tvAvailable) {
        this.tvTaken = tvTaken;
        this.tvOpening = tvOpening;
        this.tvAvailable = tvAvailable;
        this.tvDate = tvDate;
    }

    public String getTvTaken() {
        return tvTaken;
    }

    public void setTvTaken(String tvTaken) {
        this.tvTaken = tvTaken;
    }

    public String getTvOpening() {
        return tvOpening;
    }

    public void setTvOpening(String tvOpening) {
        this.tvOpening = tvOpening;
    }

    public String getTvAvailable() {
        return tvAvailable;
    }

    public void setTvAvailable(String tvAvailable) {
        this.tvAvailable = tvAvailable;
    }

    public String getTvDate() {
        return tvDate;
    }

    public void setTvDate(String tvDate) {
        this.tvDate = tvDate;
    }
}
