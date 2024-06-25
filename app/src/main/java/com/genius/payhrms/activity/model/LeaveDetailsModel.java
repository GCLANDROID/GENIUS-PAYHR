package com.genius.payhrms.activity.model;

public class LeaveDetailsModel {
    String id,leaveName,strtDay,endDay,value,reason,status,approvedDate,approvedBy,remarks;

    public LeaveDetailsModel(String id, String leaveName, String strtDay, String endDay, String value, String reason, String status, String approvedDate, String approvedBy,String remarks) {
        this.id = id;
        this.leaveName = leaveName;
        this.strtDay = strtDay;
        this.endDay = endDay;
        this.value = value;
        this.reason = reason;
        this.status = status;
        this.approvedDate = approvedDate;
        this.approvedBy = approvedBy;
        this.remarks=remarks;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeaveName() {
        return leaveName;
    }

    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
    }

    public String getStrtDay() {
        return strtDay;
    }

    public void setStrtDay(String strtDay) {
        this.strtDay = strtDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
