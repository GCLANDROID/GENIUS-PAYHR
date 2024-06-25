package com.genius.payhrms.activity.model;

public class GeoFenceApprovalModel {
    String laat,loong,address,empName,imageFile;
    boolean isSelected;
    int gID,ApproverStatus;

    public String getLaat() {
        return laat;
    }

    public void setLaat(String laat) {
        this.laat = laat;
    }

    public String getLoong() {
        return loong;
    }

    public void setLoong(String loong) {
        this.loong = loong;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getgID() {
        return gID;
    }

    public void setgID(int gID) {
        this.gID = gID;
    }

    public int getApproverStatus() {
        return ApproverStatus;
    }

    public void setApproverStatus(int approverStatus) {
        ApproverStatus = approverStatus;
    }
}
