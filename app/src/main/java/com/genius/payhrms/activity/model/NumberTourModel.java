package com.genius.payhrms.activity.model;

public class NumberTourModel {
    String date,number,minlat,minlong,maxlat,maxlong;

    public NumberTourModel(String date, String number,String minlat,String minlong,String maxlat,String maxlong) {
        this.date = date;
        this.number = number;
        this.minlat=minlat;
        this.minlong=minlong;
        this.maxlat=maxlat;
        this.maxlong=maxlong;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMinlat() {
        return minlat;
    }

    public void setMinlat(String minlat) {
        this.minlat = minlat;
    }

    public String getMinlong() {
        return minlong;
    }

    public void setMinlong(String minlong) {
        this.minlong = minlong;
    }

    public String getMaxlat() {
        return maxlat;
    }

    public void setMaxlat(String maxlat) {
        this.maxlat = maxlat;
    }

    public String getMaxlong() {
        return maxlong;
    }

    public void setMaxlong(String maxlong) {
        this.maxlong = maxlong;
    }


}
