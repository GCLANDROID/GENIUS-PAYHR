package com.genius.payhrms.activity.model;

public class YearModel {
    String year,number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public YearModel(String year, String nummber) {
        this.year = year;
        this.number = nummber;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
