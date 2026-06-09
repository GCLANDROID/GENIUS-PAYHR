package com.genius.payhrms.activity.attendance.cflo.model;

public class ApplicationDetailsModel {
    String applicationType,applicationDate,startDate,endDate,reason,apporvalStatus,approverName,actionTakenDate,AID,noOfDays;
    int Isdelete;

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getApporvalStatus() {
        return apporvalStatus;
    }

    public void setApporvalStatus(String apporvalStatus) {
        this.apporvalStatus = apporvalStatus;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getActionTakenDate() {
        return actionTakenDate;
    }

    public void setActionTakenDate(String actionTakenDate) {
        this.actionTakenDate = actionTakenDate;
    }

    public int getIsdelete() {
        return Isdelete;
    }

    public void setIsdelete(int isdelete) {
        Isdelete = isdelete;
    }

    public String getAID() {
        return AID;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }
}
