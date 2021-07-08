package com.genius.hrms.activity.model;

import java.io.Serializable;

public class VisitingLocationModel implements Serializable {
    String location,time,lattitude,longitude;

    public VisitingLocationModel(String location, String time, String lattitude, String longitude) {
        this.location = location;
        this.time = time;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
