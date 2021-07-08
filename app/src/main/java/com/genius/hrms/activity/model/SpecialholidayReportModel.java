package com.genius.hrms.activity.model;

public class SpecialholidayReportModel {
    String date,remarks,status;

    public SpecialholidayReportModel(String date, String remarks, String status) {
        this.date = date;
        this.remarks = remarks;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
