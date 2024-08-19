package com.genius.payhrms.activity.model;

public class TourViewModel {
    String StartDate,EndDate,AppliedOn,Reason,ApprovalStatus;

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getAppliedOn() {
        return AppliedOn;
    }

    public void setAppliedOn(String appliedOn) {
        AppliedOn = appliedOn;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }
}
