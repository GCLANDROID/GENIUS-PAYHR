package com.genius.payhrms.activity.model;

public class TeamReportModel {
    String empName,deptName,branchName,attenDate,inTime,outTime,address,status;

    public TeamReportModel(String empName, String deptName, String branchName, String attenDate, String inTime, String outTime, String address, String status) {
        this.empName = empName;
        this.deptName = deptName;
        this.branchName = branchName;
        this.attenDate = attenDate;
        this.inTime = inTime;
        this.outTime = outTime;
        this.address = address;
        this.status = status;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAttenDate() {
        return attenDate;
    }

    public void setAttenDate(String attenDate) {
        this.attenDate = attenDate;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
