package com.genius.payhrms.activity.model;

public class MulFenceConfigModel {
    String craetdOn,address, fencePoint,fenceId,pointNumber;

    public MulFenceConfigModel(String craetdOn, String address, String gencePoint,String fenceId,String pointNumber) {
        this.craetdOn = craetdOn;
        this.address = address;
        this.fencePoint = gencePoint;
        this.fenceId=fenceId;
        this.pointNumber=pointNumber;
    }

    public String getCraetdOn() {
        return craetdOn;
    }

    public void setCraetdOn(String craetdOn) {
        this.craetdOn = craetdOn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFencePoint() {
        return fencePoint;
    }

    public void setFencePoint(String fencePoint) {
        this.fencePoint = fencePoint;
    }

    public String getFenceId() {
        return fenceId;
    }

    public void setFenceId(String fenceId) {
        this.fenceId = fenceId;
    }

    public String getPointNumber() {
        return pointNumber;
    }

    public void setPointNumber(String pointNumber) {
        this.pointNumber = pointNumber;
    }
}
