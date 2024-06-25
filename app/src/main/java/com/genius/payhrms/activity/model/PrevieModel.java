package com.genius.payhrms.activity.model;

public class PrevieModel {
    String leaveType,strtDate,endDate,balance,reason;

    public PrevieModel(String leaveType, String strtDate, String endDate, String balance, String reason) {
        this.leaveType = leaveType;
        this.strtDate = strtDate;
        this.endDate = endDate;
        this.balance = balance;
        this.reason = reason;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getStrtDate() {
        return strtDate;
    }

    public void setStrtDate(String strtDate) {
        this.strtDate = strtDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
