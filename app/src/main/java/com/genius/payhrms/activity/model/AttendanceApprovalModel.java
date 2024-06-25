package com.genius.payhrms.activity.model;

public class AttendanceApprovalModel {
    String aId,name,attenDate,inTime,outTime,type,nature;
    private boolean isSelected = false;
    String Code;


    public AttendanceApprovalModel(String aId, String name, String attenDate, String inTime, String outTime, String type, String nature) {
        this.aId = aId;
        this.name = name;
        this.attenDate = attenDate;
        this.inTime = inTime;
        this.outTime = outTime;
        this.type = type;
        this.nature = nature;
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttenDate() {
        return attenDate;
    }

    public void setAttenDate(String attenDate) {
        this.attenDate = attenDate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
