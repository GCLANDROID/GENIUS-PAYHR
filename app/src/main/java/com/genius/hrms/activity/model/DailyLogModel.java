package com.genius.hrms.activity.model;

public class DailyLogModel {
    String activityDate,activityInTime,activityOutTime,activityInLocation,activityOutLocation,inUrl,outUrl,inRemarks,outRemarks;

    public DailyLogModel(String activityDate, String activityInTime, String activityOutTime, String activityInLocation, String activityOutLocation, String inUrl, String outUrl,String inRemarks,String outRemarks) {
        this.activityDate = activityDate;
        this.activityInTime = activityInTime;
        this.activityOutTime = activityOutTime;
        this.activityInLocation = activityInLocation;
        this.activityOutLocation = activityOutLocation;
        this.inUrl = inUrl;
        this.outUrl = outUrl;
        this.inRemarks=inRemarks;
        this.outRemarks=outRemarks;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityInTime() {
        return activityInTime;
    }

    public void setActivityInTime(String activityInTime) {
        this.activityInTime = activityInTime;
    }

    public String getActivityOutTime() {
        return activityOutTime;
    }

    public void setActivityOutTime(String activityOutTime) {
        this.activityOutTime = activityOutTime;
    }

    public String getActivityInLocation() {
        return activityInLocation;
    }

    public void setActivityInLocation(String activityInLocation) {
        this.activityInLocation = activityInLocation;
    }

    public String getActivityOutLocation() {
        return activityOutLocation;
    }

    public void setActivityOutLocation(String activityOutLocation) {
        this.activityOutLocation = activityOutLocation;
    }

    public String getInUrl() {
        return inUrl;
    }

    public void setInUrl(String inUrl) {
        this.inUrl = inUrl;
    }

    public String getOutUrl() {
        return outUrl;
    }

    public void setOutUrl(String outUrl) {
        this.outUrl = outUrl;
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
}
