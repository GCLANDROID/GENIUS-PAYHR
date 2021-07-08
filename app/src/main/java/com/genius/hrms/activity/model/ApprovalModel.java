package com.genius.hrms.activity.model;

public class ApprovalModel {
    String mId,empName,leave,startDate,endDate,value,reason,approvalStatus;
    private boolean isSelected = false;

    public ApprovalModel(String mId, String empName, String leave, String startDate, String endDate, String value, String reason,String approvalStatus) {
        this.mId = mId;
        this.empName = empName;
        this.leave = leave;
        this.startDate = startDate;
        this.endDate = endDate;
        this.value = value;
        this.reason = reason;
        this.approvalStatus=approvalStatus;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
