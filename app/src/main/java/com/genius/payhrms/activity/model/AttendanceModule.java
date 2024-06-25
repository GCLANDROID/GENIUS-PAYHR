package com.genius.payhrms.activity.model;

public class AttendanceModule {
    String attendanceDate,inTime,outTime,inAddress,outAddress,inRemarks,outRemarks,approvalStatus,inImageUrl,outImageUrl;

    public AttendanceModule(String attendanceDate, String inTime, String outTime, String inAddress, String outAddress, String inRemarks, String outRemarks, String approvalStatus,String inImageUrl,String outImageUrl) {
        this.attendanceDate = attendanceDate;
        this.inTime = inTime;
        this.outTime = outTime;
        this.inAddress = inAddress;
        this.outAddress = outAddress;
        this.inRemarks = inRemarks;
        this.outRemarks = outRemarks;
        this.approvalStatus = approvalStatus;
        this.inImageUrl=inImageUrl;
        this.outImageUrl=outImageUrl;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getInAddress() {
        return inAddress;
    }

    public void setInAddress(String inAddress) {
        this.inAddress = inAddress;
    }

    public String getOutAddress() {
        return outAddress;
    }

    public void setOutAddress(String outAddress) {
        this.outAddress = outAddress;
    }

    public String getInRemarks() {
        return inRemarks;
    }

    public void setInRemarks(String inRemarks) {
        this.inRemarks = inRemarks;
    }

    public String getOutRemarks() {
        return outRemarks;
    }

    public void setOutRemarks(String outRemarks) {
        this.outRemarks = outRemarks;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getInImageUrl() {
        return inImageUrl;
    }

    public void setInImageUrl(String inImageUrl) {
        this.inImageUrl = inImageUrl;
    }

    public String getOutImageUrl() {
        return outImageUrl;
    }

    public void setOutImageUrl(String outImageUrl) {
        this.outImageUrl = outImageUrl;
    }
}
