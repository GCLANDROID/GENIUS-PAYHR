package com.genius.payhrms.activity.model;

public class OfflineDailyLogModel {
    String activityDate,activityInTime,activityInLocation,inRemarks;

    public OfflineDailyLogModel(String activityDate, String activityInTime,  String activityInLocation,  String inRemarks) {
        this.activityDate = activityDate;
        this.activityInTime = activityInTime;

        this.activityInLocation = activityInLocation;

        this.inRemarks=inRemarks;

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



    public String getActivityInLocation() {
        return activityInLocation;
    }

    public void setActivityInLocation(String activityInLocation) {
        this.activityInLocation = activityInLocation;
    }




    public String getInRemarks() {
        return inRemarks;
    }

    public void setInRemarks(String inRemarks) {
        this.inRemarks = inRemarks;
    }


}
