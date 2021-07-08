package com.genius.hrms.activity.model;

public class OfflineDailyModel {
    String date,inTime,outTime,inLocation,outLocation,inRemarks,outRemarks,imgUrlIN,imgUrlOUT,docUrl;
    String costCenter;

    public OfflineDailyModel(String date, String inTime, String outTime, String inLocation, String outLocation, String inRemarks, String outRemarks,String inImage,String outImage) {
        this.date = date;
        this.inTime = inTime;
        this.outTime = outTime;
        this.inLocation = inLocation;
        this.outLocation = outLocation;
        this.inRemarks = inRemarks;
        this.outRemarks = outRemarks;
        this.imgUrlIN=inImage;
        this.imgUrlOUT=outImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getInLocation() {
        return inLocation;
    }

    public void setInLocation(String inLocation) {
        this.inLocation = inLocation;
    }

    public String getOutLocation() {
        return outLocation;
    }

    public void setOutLocation(String outLocation) {
        this.outLocation = outLocation;
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

    public String getImgUrlIN() {
        return imgUrlIN;
    }

    public void setImgUrlIN(String imgUrlIN) {
        this.imgUrlIN = imgUrlIN;
    }

    public String getImgUrlOUT() {
        return imgUrlOUT;
    }

    public void setImgUrlOUT(String imgUrlOUT) {
        this.imgUrlOUT = imgUrlOUT;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }
}
