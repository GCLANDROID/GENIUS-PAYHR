package com.genius.payhrms.activity.model;

public class DistanceModel {
    String EmployeeID,EmployeeName,Attendnacedate,Address,DistanceInKM;

    public DistanceModel(String employeeID, String employeeName, String attendnacedate, String address, String distanceInKM) {
        EmployeeID = employeeID;
        EmployeeName = employeeName;
        Attendnacedate = attendnacedate;
        Address = address;
        DistanceInKM = distanceInKM;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getAttendnacedate() {
        return Attendnacedate;
    }

    public void setAttendnacedate(String attendnacedate) {
        Attendnacedate = attendnacedate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDistanceInKM() {
        return DistanceInKM;
    }

    public void setDistanceInKM(String distanceInKM) {
        DistanceInKM = distanceInKM;
    }
}
