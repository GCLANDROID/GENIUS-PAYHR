package com.genius.payhrms.activity.model;

public class SpecialHolidayApprovalItemModel {
    String aId,name,date,remarks;
    private boolean isSelected = false;


    public SpecialHolidayApprovalItemModel(String aId, String name, String date, String remarks) {
        this.aId = aId;
        this.name = name;
        this.date = date;
        this.remarks = remarks;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
