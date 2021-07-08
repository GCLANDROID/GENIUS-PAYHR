package com.genius.hrms.activity.model;

public class AttendanceModuleForPPS {
    String attendanceDate,attendanceInTime,attendanceOutTime,attendanceLocation,attendanceReply,attendanceType;

    public AttendanceModuleForPPS(String attendanceDate, String attendanceInTime, String attendanceOutTime, String attendanceLocation, String attendanceReply, String attendanceType) {
        this.attendanceDate = attendanceDate;
        this.attendanceInTime = attendanceInTime;
        this.attendanceOutTime = attendanceOutTime;
        this.attendanceLocation = attendanceLocation;
        this.attendanceReply = attendanceReply;
        this.attendanceType = attendanceType;
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
}
