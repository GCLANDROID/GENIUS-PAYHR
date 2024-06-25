package com.genius.payhrms.activity.model;

public class AttendanceRegulizationModel {
    String date,sysInTime,sysOutTime,appinTime,appOutTime,remarks;

    public AttendanceRegulizationModel(String date, String sysInTime, String sysOutTime, String appinTime, String appOutTime) {
        this.date = date;
        this.sysInTime = sysInTime;
        this.sysOutTime = sysOutTime;
        this.appinTime = appinTime;
        this.appOutTime = appOutTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSysInTime() {
        return sysInTime;
    }

    public void setSysInTime(String sysInTime) {
        this.sysInTime = sysInTime;
    }

    public String getSysOutTime() {
        return sysOutTime;
    }

    public void setSysOutTime(String sysOutTime) {
        this.sysOutTime = sysOutTime;
    }

    public String getAppinTime() {
        return appinTime;
    }

    public void setAppinTime(String appinTime) {
        this.appinTime = appinTime;
    }

    public String getAppOutTime() {
        return appOutTime;
    }

    public void setAppOutTime(String appOutTime) {
        this.appOutTime = appOutTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
