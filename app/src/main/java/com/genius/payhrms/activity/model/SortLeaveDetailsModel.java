package com.genius.payhrms.activity.model;

public class SortLeaveDetailsModel {
    String EmployeeName,Year,Month,ApplicationDate,ShortLeaveMins,Status,Reason,ApprovedBy,ApprovedOn,AID;

    public SortLeaveDetailsModel(String employeeName, String year, String month, String applicationDate, String shortLeaveMins, String status, String reason, String approvedBy, String approvedOn,String AID) {
        EmployeeName = employeeName;
        Year = year;
        Month = month;
        ApplicationDate = applicationDate;
        ShortLeaveMins = shortLeaveMins;
        Status = status;
        Reason = reason;
        ApprovedBy = approvedBy;
        ApprovedOn = approvedOn;
        this.AID = AID;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getApplicationDate() {
        return ApplicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        ApplicationDate = applicationDate;
    }

    public String getShortLeaveMins() {
        return ShortLeaveMins;
    }

    public void setShortLeaveMins(String shortLeaveMins) {
        ShortLeaveMins = shortLeaveMins;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getApprovedBy() {
        return ApprovedBy;
    }

    public void setApprovedBy(String approvedBy) {
        ApprovedBy = approvedBy;
    }

    public String getApprovedOn() {
        return ApprovedOn;
    }

    public void setApprovedOn(String approvedOn) {
        ApprovedOn = approvedOn;
    }

    public String getAID() {
        return AID;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }
}
