package com.genius.payhrms.activity.model;

public class OtherApproverModel {
    public int SL;
    public String AID;
    public String Name;

    public String AdjustmentType;

    public String EmpCode;

    public String AppliedDate;

    public String StartDate;

    public String EndDate;

    public Object InTime;

    public Object OutTime;

    public String NoOfDays;

    public String Reason;

    public String clientname;

    public String Destination;

    public String TravelDetails;

    public String AdvanceAmount;

    public String ApprovedBY="";

    public String ApprovedOn;

    public String ApprovalStatus;

    public int Isdelete;

    public int OD;

    public int IsApprove;

    public String ApprovalRemarks;

    public String DocumentName;

    public String Documentlink;

    private boolean isSelected = false;
    public String offdate;

    public OtherApproverModel(int SL, String AID, String name, String adjustmentType, String empCode, String appliedDate, String startDate, String endDate, Object inTime, Object outTime, String noOfDays, String reason, String clientname, String destination, String travelDetails, String advanceAmount, String approvedBY, String approvedOn, String approvalStatus, int isdelete, int OD, int isApprove, String approvalRemarks, String documentName, String documentlink, String offdate) {
        this.SL = SL;
        this.AID = AID;
        Name = name;
        AdjustmentType = adjustmentType;
        EmpCode = empCode;
        AppliedDate = appliedDate;
        StartDate = startDate;
        EndDate = endDate;
        InTime = inTime;
        OutTime = outTime;
        NoOfDays = noOfDays;
        Reason = reason;
        this.clientname = clientname;
        Destination = destination;
        TravelDetails = travelDetails;
        AdvanceAmount = advanceAmount;
        ApprovedBY = approvedBY;
        ApprovedOn = approvedOn;
        ApprovalStatus = approvalStatus;
        Isdelete = isdelete;
        this.OD = OD;
        IsApprove = isApprove;
        ApprovalRemarks = approvalRemarks;
        DocumentName = documentName;
        Documentlink = documentlink;
        this.offdate = offdate;
    }

    public int getSL() {
        return SL;
    }

    public void setSL(int SL) {
        this.SL = SL;
    }

    public String getAID() {
        return AID;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAdjustmentType() {
        return AdjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        AdjustmentType = adjustmentType;
    }

    public String getEmpCode() {
        return EmpCode;
    }

    public void setEmpCode(String empCode) {
        EmpCode = empCode;
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

    public Object getInTime() {
        return InTime;
    }

    public void setInTime(Object inTime) {
        InTime = inTime;
    }

    public Object getOutTime() {
        return OutTime;
    }

    public void setOutTime(Object outTime) {
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
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
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

    public int getIsApprove() {
        return IsApprove;
    }

    public void setIsApprove(int isApprove) {
        IsApprove = isApprove;
    }

    public String getApprovalRemarks() {
        return ApprovalRemarks;
    }

    public void setApprovalRemarks(String approvalRemarks) {
        ApprovalRemarks = approvalRemarks;
    }

    public String getDocumentName() {
        return DocumentName;
    }

    public void setDocumentName(String documentName) {
        DocumentName = documentName;
    }

    public Object getDocumentlink() {
        return Documentlink;
    }

    public void setDocumentlink(String documentlink) {
        Documentlink = documentlink;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getOffdate() {
        return offdate;
    }

    public void setOffdate(String offdate) {
        this.offdate = offdate;
    }
}
