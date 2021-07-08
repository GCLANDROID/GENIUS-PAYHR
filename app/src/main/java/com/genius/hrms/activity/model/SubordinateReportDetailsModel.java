package com.genius.hrms.activity.model;

public class SubordinateReportDetailsModel {
    String EmpName,Address,FenceTime,FenceType;

    public SubordinateReportDetailsModel(String empName, String address, String fenceTime, String fenceType) {
        EmpName = empName;
        Address = address;
        FenceTime = fenceTime;
        FenceType = fenceType;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getFenceTime() {
        return FenceTime;
    }

    public void setFenceTime(String fenceTime) {
        FenceTime = fenceTime;
    }

    public String getFenceType() {
        return FenceType;
    }

    public void setFenceType(String fenceType) {
        FenceType = fenceType;
    }
}
