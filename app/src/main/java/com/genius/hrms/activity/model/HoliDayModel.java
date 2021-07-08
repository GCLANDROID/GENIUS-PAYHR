package com.genius.hrms.activity.model;

public class HoliDayModel {
    String purpose,holidayDate,holidayDay;

    public HoliDayModel(String purpose, String holidayDate, String holidayDay) {
        this.purpose = purpose;
        this.holidayDate = holidayDate;
        this.holidayDay = holidayDay;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getHolidayDay() {
        return holidayDay;
    }

    public void setHolidayDay(String holidayDay) {
        this.holidayDay = holidayDay;
    }
}
