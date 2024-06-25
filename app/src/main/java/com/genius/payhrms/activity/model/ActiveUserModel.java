package com.genius.payhrms.activity.model;

public class ActiveUserModel {
    String loginTime,device,name;

    public ActiveUserModel(String loginTime, String device, String name) {
        this.loginTime = loginTime;
        this.device = device;
        this.name = name;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
