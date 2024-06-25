package com.genius.payhrms.activity.model;

public class DailyActivityModel {
    String activityDate,activityInTime,activityOutTime,projectName,subProjectName,location,imgUrl;

    public DailyActivityModel(String activityDate, String activityInTime, String activityOutTime, String projectName, String subProjectName, String location,String imgUrl) {
        this.activityDate = activityDate;
        this.activityInTime = activityInTime;
        this.activityOutTime = activityOutTime;
        this.projectName = projectName;
        this.subProjectName = subProjectName;
        this.location = location;
        this.imgUrl=imgUrl;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSubProjectName() {
        return subProjectName;
    }

    public void setSubProjectName(String subProjectName) {
        this.subProjectName = subProjectName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
