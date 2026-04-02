package com.genius.payhrms.activity.model;

public class SortLeaveApproverModel {
    String employeeID,employeeName,AID,yearID,month,applicationDate,ahortLeaveMins,status,reason,approvedBy,approvedOn;
    boolean isSelected = false;

    public SortLeaveApproverModel(String employeeID, String employeeName, String AID, String yearID, String month, String applicationDate, String ahortLeaveMins, String status, String reason, String approvedBy, String approvedOn) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.AID = AID;
        this.yearID = yearID;
        this.month = month;
        this.applicationDate = applicationDate;
        this.ahortLeaveMins = ahortLeaveMins;
        this.status = status;
        this.reason = reason;
        this.approvedBy = approvedBy;
        this.approvedOn = approvedOn;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAID() {
        return AID;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }

    public String getYearID() {
        return yearID;
    }

    public void setYearID(String yearID) {
        this.yearID = yearID;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getAhortLeaveMins() {
        return ahortLeaveMins;
    }

    public void setAhortLeaveMins(String ahortLeaveMins) {
        this.ahortLeaveMins = ahortLeaveMins;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(String approvedOn) {
        this.approvedOn = approvedOn;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
