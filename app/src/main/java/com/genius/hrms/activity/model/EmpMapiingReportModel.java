package com.genius.hrms.activity.model;

public class EmpMapiingReportModel {
    String empName,location;


    public EmpMapiingReportModel(String empName,String location) {
        this.empName = empName;
        this.location=location;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
