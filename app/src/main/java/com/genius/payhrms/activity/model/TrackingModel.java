package com.genius.payhrms.activity.model;

public class TrackingModel {
    String empname,trackingId;

    public TrackingModel(String empname, String trackingId) {
        this.empname = empname;
        this.trackingId = trackingId;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }
}
