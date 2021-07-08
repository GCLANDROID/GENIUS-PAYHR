package com.genius.hrms.activity.model;

public class FenceNumberModel {
    String date,fenceNumber;

    public FenceNumberModel(String date, String fenceNumber) {
        this.date = date;
        this.fenceNumber = fenceNumber;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFenceNumber() {
        return fenceNumber;
    }

    public void setFenceNumber(String fenceNumber) {
        this.fenceNumber = fenceNumber;
    }
}
