package com.genius.hrms.activity.model;

public class AttendanceApprovalModule {
    String attendanceDate,attendanceInTime,attendanceOutTime,attendanceLocation,attendanceReply,attendanceType,attId,empName,empId;
    private boolean isSelected = false;

    public AttendanceApprovalModule(String attendanceDate, String attendanceInTime, String attendanceOutTime, String attendanceLocation, String attendanceReply, String attendanceType, String attId, String empName, String empId) {
        this.attendanceDate = attendanceDate;
        this.attendanceInTime = attendanceInTime;
        this.attendanceOutTime = attendanceOutTime;
        this.attendanceLocation = attendanceLocation;
        this.attendanceReply = attendanceReply;
        this.attendanceType = attendanceType;
        this.attId = attId;
        this.empName = empName;
        this.empId = empId;

    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getAttendanceInTime() {
        return attendanceInTime;
    }

    public void setAttendanceInTime(String attendanceInTime) {
        this.attendanceInTime = attendanceInTime;
    }

    public String getAttendanceOutTime() {
        return attendanceOutTime;
    }

    public void setAttendanceOutTime(String attendanceOutTime) {
        this.attendanceOutTime = attendanceOutTime;
    }

    public String getAttendanceLocation() {
        return attendanceLocation;
    }

    public void setAttendanceLocation(String attendanceLocation) {
        this.attendanceLocation = attendanceLocation;
    }

    public String getAttendanceReply() {
        return attendanceReply;
    }

    public void setAttendanceReply(String attendanceReply) {
        this.attendanceReply = attendanceReply;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAttId() {
        return attId;
    }

    public void setAttId(String attId) {
        this.attId = attId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
}
