package com.genius.hrms.activity.model;

public class EmployeeMapiingModule {
    String empName,empId,location;
    private boolean isSelected = false;

    public EmployeeMapiingModule(String empName, String empId,String location) {
        this.empName = empName;
        this.empId = empId;
        this.location=location;

    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
