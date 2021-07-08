package com.genius.hrms.activity.model;

public class HolidayMapModel {
    String holidayId,holidayName,holidayDate;

    public HolidayMapModel(String holidayId, String holidayName, String holidayDate) {
        this.holidayId = holidayId;
        this.holidayName = holidayName;
        this.holidayDate = holidayDate;
    }

    public String getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(String holidayId) {
        this.holidayId = holidayId;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }
}
