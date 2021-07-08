package com.genius.hrms.activity.model;

public class SalaryModule {
    String year,month,amount,surl;

    public SalaryModule(String year, String month, String amount,String surl) {
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.surl=surl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }
}
