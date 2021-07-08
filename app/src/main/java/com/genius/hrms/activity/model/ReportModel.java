package com.genius.hrms.activity.model;

public class ReportModel {
    String Address,FenceType,logTime,logDate;

    public ReportModel(String address, String fenceType, String logTime, String logDate) {
        Address = address;
        FenceType = fenceType;
        this.logTime = logTime;
        this.logDate = logDate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getFenceType() {
        return FenceType;
    }

    public void setFenceType(String fenceType) {
        FenceType = fenceType;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }
}
