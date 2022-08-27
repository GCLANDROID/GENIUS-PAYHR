package com.genius.hrms.activity.model;

public class MonthModel {
    String monthName,monthId;

    public MonthModel(String monthName, String monthId) {
        this.monthName = monthName;
        this.monthId = monthId;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthId() {
        return monthId;
    }

    public void setMonthId(String monthId) {
        this.monthId = monthId;
    }
}
