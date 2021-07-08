package com.genius.hrms.activity.model;

public class SpecialHolidayApprovalReportModel {
    String empName,date,remarks,status;

    public SpecialHolidayApprovalReportModel(String empName, String date, String remarks, String status) {
        this.empName = empName;
        this.date = date;
        this.remarks = remarks;
        this.status = status;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
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
