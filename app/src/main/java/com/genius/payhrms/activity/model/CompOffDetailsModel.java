package com.genius.payhrms.activity.model;

public class CompOffDetailsModel {
    String brkUpDate,leaveValue,dayValue;
    private boolean isSelected = false;

    public CompOffDetailsModel(String brkUpDate, String leaveValue) {
        this.brkUpDate = brkUpDate;
        this.leaveValue = leaveValue;

    }

    public String getBrkUpDate() {
        return brkUpDate;
    }

    public void setBrkUpDate(String brkUpDate) {
        this.brkUpDate = brkUpDate;
    }

    public String getLeaveValue() {
        return leaveValue;
    }

    public void setLeaveValue(String leaveValue) {
        this.leaveValue = leaveValue;
    }



    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDayValue() {
        return dayValue;
    }

    public void setDayValue(String dayValue) {
        this.dayValue = dayValue;
    }
}
