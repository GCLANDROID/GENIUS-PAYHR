package com.genius.payhrms.activity.model;

public class AdjustmentDetailsModel {
    public String AID;
    public String AdjustmentType;
    public String AppliedDate;

    public String StartDate;

    public String EndDate;

    public String InTime;

    public String OutTime;

    public String NoOfDays;

    public String Reason;

    public String Clientname;

    public String Destination;

    public String TravelDetails;

    public String AdvanceAmount;

    public String ApprovedBY;

    public String ApprovedOn;

    public String ApprovalStatus;

    public int Isdelete;

    public int OD;
    public String offdate;

    public AdjustmentDetailsModel(String AID, String adjustmentType, String appliedDate, String startDate, String endDate, String inTime, String outTime, String noOfDays, String reason, String clientname, String destination, String travelDetails, String advanceAmount, String approvedBY, String approvedOn, String approvalStatus, int isdelete, int OD, String offdate) {
        this.AID = AID;
        AdjustmentType = adjustmentType;
        AppliedDate = appliedDate;
        StartDate = startDate;
        EndDate = endDate;
        InTime = inTime;
        OutTime = outTime;
        NoOfDays = noOfDays;
        Reason = reason;
        Clientname = clientname;
        Destination = destination;
        TravelDetails = travelDetails;
        AdvanceAmount = advanceAmount;
        ApprovedBY = approvedBY;
        ApprovedOn = approvedOn;
        ApprovalStatus = approvalStatus;
        Isdelete = isdelete;
        this.OD = OD;
        this.offdate = offdate;
    }

    public String getAID() {
        return AID;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }

    public String getAdjustmentType() {
        return AdjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        AdjustmentType = adjustmentType;
    }

    public String getAppliedDate() {
        return AppliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        AppliedDate = appliedDate;
    }

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

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getNoOfDays() {
        return NoOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        NoOfDays = noOfDays;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getClientname() {
        return Clientname;
    }

    public void setClientname(String clientname) {
        Clientname = clientname;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getTravelDetails() {
        return TravelDetails;
    }

    public void setTravelDetails(String travelDetails) {
        TravelDetails = travelDetails;
    }

    public String getAdvanceAmount() {
        return AdvanceAmount;
    }

    public void setAdvanceAmount(String advanceAmount) {
        AdvanceAmount = advanceAmount;
    }

    public String getApprovedBY() {
        return ApprovedBY;
    }

    public void setApprovedBY(String approvedBY) {
        ApprovedBY = approvedBY;
    }

    public String getApprovedOn() {
        return ApprovedOn;
    }

    public void setApprovedOn(String approvedOn) {
        ApprovedOn = approvedOn;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public int getIsdelete() {
        return Isdelete;
    }

    public void setIsdelete(int isdelete) {
        Isdelete = isdelete;
    }

    public int getOD() {
        return OD;
    }

    public void setOD(int OD) {
        this.OD = OD;
    }
}
