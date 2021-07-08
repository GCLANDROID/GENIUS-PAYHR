package com.genius.hrms.activity.model;

public class ConfigReportModel {
    String createdOn,locationName,sLat,sLong,address,endPoint;

    public ConfigReportModel(String createdOn, String locationName, String sLat, String sLong, String address,String endpoint) {
        this.createdOn = createdOn;
        this.locationName = locationName;
        this.sLat = sLat;
        this.sLong = sLong;
        this.address = address;
        this.endPoint=endpoint;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getsLat() {
        return sLat;
    }

    public void setsLat(String sLat) {
        this.sLat = sLat;
    }

    public String getsLong() {
        return sLong;
    }

    public void setsLong(String sLong) {
        this.sLong = sLong;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
