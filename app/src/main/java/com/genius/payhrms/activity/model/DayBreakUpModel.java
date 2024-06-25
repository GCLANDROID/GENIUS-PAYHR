package com.genius.payhrms.activity.model;

public class DayBreakUpModel {
    String brkupDate,dateName,dayAccess,dayAccessDesc,dayModeValue,balance;
    private boolean isSelected = false;

    public DayBreakUpModel(String brkupDate, String dateName, String dayAccess,String dayAccessDesc) {
        this.brkupDate = brkupDate;
        this.dateName = dateName;
        this.dayAccess = dayAccess;
        this.dayAccessDesc=dayAccessDesc;
    }

    public String getBrkupDate() {
        return brkupDate;
    }

    public void setBrkupDate(String brkupDate) {
        this.brkupDate = brkupDate;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public String getDayAccess() {
        return dayAccess;
    }

    public void setDayAccess(String dayAccess) {
        this.dayAccess = dayAccess;
    }

    public String getDayAccessDesc() {
        return dayAccessDesc;
    }

    public void setDayAccessDesc(String dayAccessDesc) {
        this.dayAccessDesc = dayAccessDesc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDayModeValue() {
        return dayModeValue;
    }

    public void setDayModeValue(String dayModeValue) {
        this.dayModeValue = dayModeValue;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
