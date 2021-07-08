package com.genius.hrms.activity.model;

public class SpecialHolidayItemModel {
    String date,dayName;
    private boolean isSelected = false;

    public SpecialHolidayItemModel(String date, String dayName) {
        this.date = date;
        this.dayName = dayName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
